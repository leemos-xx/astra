package leemos.astra.core;

import java.util.Timer;

import leemos.astra.*;
import leemos.astra.event.Event;
import leemos.astra.event.EventBus;
import leemos.astra.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leemos.astra.rpc.RequestVoteReq;
import leemos.astra.rpc.RequestVoteResp;

/**
 * AbstractNode 用于控制Node的状态切换
 * 
 * @author lihao
 * @date 2022-02-27
 * @version 1.0
 */
public abstract class StatefulNode implements Node {

    protected static final Logger logger = LoggerFactory.getLogger(Node.class);

    protected NodeConfig config;
    private volatile NodeState state;

    private Timer electionTimer;
    private Timer heartbeatTimer;

    public StatefulNode(NodeConfig config) {
        this.config = config;
        Consensus.init(config.getPeers().length);

        // 通过内部事件总线来解耦Node的状态变化
        EventBus.get().addListener(new EventListener() {

            @Override
            public void fireEvent(Event event) {
                switch (event.getType()) {
                    case CONVERSION_TO_FOLLOWER:
                        conversionTo(NodeState.FOLLOWER);
                        break;
                    case CONVERSION_TO_CANDIDATE:
                        conversionTo(NodeState.CANDIDATE);
                        break;
                    case CONVERSION_TO_LEADER:
                        conversionTo(NodeState.LEADER);
                        break;
                }
            }
        });
    }

    @Override
    public NodeConfig getConfig() {
        return config;
    }

    private synchronized void conversionTo(NodeState newState) {
        if (this.state == NodeState.LEADER) {
            resignFromLeader();
        }
        if (this.state == NodeState.CANDIDATE) {
            resignFromCandidate();
        }
        if (this.state == NodeState.FOLLOWER) {
            resignFromFollower();
        }

        this.state = newState;

        switch (newState) {
        case FOLLOWER:
            conversionToFollower();
            break;
        case CANDIDATE:
            conversionToCandidate();
            break;
        case LEADER:
            conversionToLeader();
            break;
        default:
            break;
        }

    }

    private void conversionToLeader() {
        logger.info("node conversion to leader{term:{}, commitIndex:{}, lastApplied:{}, voteFor:{}}...",
                Consensus.get().getCurrentTerm(), Consensus.get().getCommitIndex(), Consensus.get().getLastApplied(), Consensus.get().getVoteFor());

        int heartbeatTimeout = getConfig().getHeartbeatTimeout();
        heartbeatTimer = new Timer();
        heartbeatTimer.scheduleAtFixedRate(new HeartbeatTask(this), 0, heartbeatTimeout);
    }

    private void conversionToCandidate() {
        logger.info("node conversion to candidate{term:{}, commitIndex:{}, lastApplied:{}, voteFor:{}}...",
                Consensus.get().getCurrentTerm(), Consensus.get().getCommitIndex(), Consensus.get().getLastApplied(), Consensus.get().getVoteFor());

        // 切换为候选人身份后，将term自增1，并发起新一轮选举
        Consensus.get().increaseTeam();

        // 投票给自己，并争取其它节点的投票
        int votes = 1;
        Consensus.get().voteFor(getId());

        for (Client client : getClients()) {
            RequestVoteReq request = RequestVoteReq.builder()
                    .term(Consensus.get().getCurrentTerm())
                    .candidateId(getId())
                    .lastLogIndex(StandardLog.get().last().getLogIndex())
                    .lastLogTerm(StandardLog.get().last().getTerm())
                    .build();
            try {
                RequestVoteResp response = client.requestVote(request);

                // 如果某个节点的任期比自己大，则结束选举
                if (response.getTerm() > Consensus.get().getCurrentTerm()) {
                    conversionTo(NodeState.FOLLOWER);
                    break;
                }

                if (response.isVoteGranted()) {
                    votes++;
                }
            } catch (Exception e) {
                logger.error("rpc error: " + e.getMessage());
            }

        }

        // 是否获得超过半数的投票
        if (votes > (getConfig().getPeers().length + 1) / 2) {
            conversionTo(NodeState.LEADER);
        } else {
            conversionTo(NodeState.FOLLOWER);
            Consensus.get().decreaseTerm();
        }

    }

    private void conversionToFollower() {
        logger.info("node conversion to follower{term:{}, commitIndex:{}, lastApplied:{}, voteFor:{}}...",
                Consensus.get().getCurrentTerm(), Consensus.get().getCommitIndex(), Consensus.get().getLastApplied(), Consensus.get().getVoteFor());
        
        int electionTimeout = getConfig().getElectionTimeout();
        electionTimer = new Timer();
        electionTimer.scheduleAtFixedRate(new ElectionTask(), electionTimeout, electionTimeout);
    }

    private void resignFromLeader() {
        logger.info("Node resign from leader{term:{}, commitIndex:{}, lastApplied:{}, voteFor:{}}...",
                Consensus.get().getCurrentTerm(), Consensus.get().getCommitIndex(), Consensus.get().getLastApplied(), Consensus.get().getVoteFor());
        // 不再继续向其它节点发送心跳
        heartbeatTimer.cancel();
    }

    private void resignFromCandidate() {
        logger.info("Node resign from candidate{term:{}, commitIndex:{}, lastApplied:{}, voteFor:{}}...",
                Consensus.get().getCurrentTerm(), Consensus.get().getCommitIndex(), Consensus.get().getLastApplied(), Consensus.get().getVoteFor());
    }

    private void resignFromFollower() {
        logger.info("Node resign from follower{term:{}, commitIndex:{}, lastApplied:{}, voteFor:{}}...",
                Consensus.get().getCurrentTerm(), Consensus.get().getCommitIndex(), Consensus.get().getLastApplied(), Consensus.get().getVoteFor());
        // 不再随机时间发起选举
        electionTimer.cancel();
    }
}
