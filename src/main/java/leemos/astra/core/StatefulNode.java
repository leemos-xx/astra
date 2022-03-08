package leemos.astra.core;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leemos.astra.Client;
import leemos.astra.Consensus;
import leemos.astra.Log;
import leemos.astra.LogEntry;
import leemos.astra.Node;
import leemos.astra.StateMachine;
import leemos.astra.rpc.AppendEntriesReq;
import leemos.astra.rpc.AppendEntriesResp;
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

    private volatile NodeState state;
    private Timer electionTimer = new Timer();
    private Timer heartbeatTimer = new Timer();

    private Consensus consensus;
    private Log log;
    private StateMachine stateMachine;

    private Lock lock = new ReentrantLock();

    public StatefulNode() {
        consensus = new Consensus(getConfig().getPeers().length);
    }

    @Override
    public Consensus getConsensus() {
        return consensus;
    }

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public StateMachine getStateMachine() {
        return stateMachine;
    }

    @Override
    public void conversionTo(NodeState newState) {
        if (this.state == newState) {
            return;
        }

        if (this.state == NodeState.LEADER) {
            resignFromLeader();
        }
        if (this.state == NodeState.CANDIDATE) {
            resignFromCandidate();
        }
        if (this.state == NodeState.FOLLOWER) {
            resignFromFollower();
        }

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

        consensus.voteFor(null);
        this.state = newState;
    }

    private void conversionToLeader() {
        int heartbeatTimeout = getConfig().getHeartbeatTimeout();
        heartbeatTimer.scheduleAtFixedRate(new Heartbeat(), 0, heartbeatTimeout);
    }

    private void resignFromLeader() {
        heartbeatTimer.cancel();
    }

    private void conversionToCandidate() {
        try {
            lock.lock();
            // 切换为候选人身份后，将term自增1，并发起新一轮选举
            consensus.increaseTeam();

            // 投票给自己，并争取其它节点的投票
            consensus.voteFor(getId());

            int votes = 1;
            for (Client client : getClients()) {
                RequestVoteReq request = RequestVoteReq.builder().term(consensus.getCurrentTerm()).candidateId(getId())
                        .lastLogIndex(log.last().getLogIndex()).lastLogTerm(log.last().getTerm()).build();
                RequestVoteResp response = client.requestVote(request);

                if (response.getTerm() > consensus.getCurrentTerm()) {
                    conversionTo(NodeState.FOLLOWER);
                    break;
                }

                if (response.isVoteGranted()) {
                    votes++;
                }
            }

            // 是否获得超过半数的投票
            if (votes > getConfig().getPeers().length / 2) {
                conversionTo(NodeState.LEADER);
            } else {
                conversionTo(NodeState.FOLLOWER);
            }
        } finally {
            lock.unlock();
        }
    }

    private void resignFromCandidate() {
        try {
            lock.lock();

            consensus.decreaseTerm();
            consensus.voteFor(null);
        } finally {
            lock.unlock();
        }
    }

    private void conversionToFollower() {
        int electionTimeout = getConfig().getElectionTimeout();
        electionTimer.scheduleAtFixedRate(new Election(), electionTimeout, electionTimeout);
    }

    private void resignFromFollower() {
        electionTimer.cancel();
    }

    private class Heartbeat extends TimerTask {

        @Override
        public void run() {
            if (state != NodeState.LEADER) {
                return;
            }

            outter: for (int i = 0; i < getClients().length; i++) {

                long logIndex = log.last().getLogIndex();
                while (true) {
                    LogEntry entry = log.read(logIndex);

                    AppendEntriesReq request = AppendEntriesReq.builder().term(consensus.getCurrentTerm())
                            .leaderId(getId()).prevLogIndex(entry.getLogIndex()).prevLogTerm(entry.getTerm())
                            .entries(new LogEntry[0]).leaderCommit(consensus.getCommitIndex()).build();
                    AppendEntriesResp response = getClients()[i].heartbeat(request);

                    if (response.getTerm() > consensus.getCurrentTerm()) {
                        conversionTo(NodeState.FOLLOWER);
                        break outter;
                    }

                    if (response.isSuccess()) {
                        consensus.updateCommit(i, consensus.getCommitIndex());
                        break;
                    } else {
                        logIndex--;
                        consensus.updateMatch(i, logIndex);
                    }
                }
            }
        }

    }

    private class Election extends TimerTask {

        @Override
        public void run() {
            conversionTo(NodeState.CANDIDATE);
        }

    }
}