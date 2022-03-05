package leemos.astra.node;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leemos.astra.Client;
import leemos.astra.Log;
import leemos.astra.LogEntry;
import leemos.astra.Node;
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

    private int currentTerm;
    private String voteFor;
    private Log log;
    private long commitIndex;
    private long lastApplied;
    private long[] nextIndex;
    private long[] matchIndex;

    private Lock lock = new ReentrantLock();

    protected void conversionTo(NodeState newState) {
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
            currentTerm++;

            // 投票给自己，并争取其它节点的投票
            voteFor = getId();
            int votes = 1;
            for (Client client : getClients()) {
                RequestVoteReq request = RequestVoteReq.builder().term(currentTerm).candidateId(getId())
                        .lastLogIndex(log.last().getLogIndex()).lastLogTerm(log.last().getTerm()).build();
                RequestVoteResp response = client.requestVote(request);
                if (response.getTerm() > currentTerm) {
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

            currentTerm--;
            voteFor = null;
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

            for (int i = 0; i < getClients().length; i++) {
                AppendEntriesReq request = AppendEntriesReq.builder().term(currentTerm).leaderId(getId())
                        .prevLogIndex(log.last().getLogIndex()).prevLogTerm(log.last().getTerm())
                        .entries(new LogEntry[0]).leaderCommit(commitIndex).build();
                AppendEntriesResp response = getClients()[i].heartbeat(request);
                if (response.getTerm() > currentTerm) {
                    conversionTo(NodeState.FOLLOWER);
                    break;
                }

                if (response.isSuccess()) {
                    nextIndex[i] = commitIndex;
                } else {
                    // FIXME log replication
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
