package leemos.astra.node;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leemos.astra.Node;

/**
 * AbstractNode 用于控制Node的状态切换
 * 
 * @author lihao
 * @date 2022-02-27
 * @version 1.0
 */
public abstract class StatefulNode implements Node {

    protected static final Logger logger = LoggerFactory.getLogger(Node.class);
    
    private NodeState state;
    private Timer electionTimer = new Timer();
    private Timer heartbeatTimer = new Timer();

    protected synchronized void conversionTo(NodeState newState) {
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
        heartbeatTimer.scheduleAtFixedRate(new Heartbeat(this), 0, heartbeatTimeout);
    }

    private void resignFromLeader() {
        heartbeatTimer.cancel();
    }

    private void conversionToCandidate() {
        getClients().requestVote();
    }

    // FIXME 是否需要此方法
    private void resignFromCandidate() {

    }

    private void conversionToFollower() {
        int electionTimeout = getConfig().getElectionTimeout();
        electionTimer.scheduleAtFixedRate(new Election(this), electionTimeout, electionTimeout);
    }

    private void resignFromFollower() {
        electionTimer.cancel();
    }

    private class Heartbeat extends TimerTask {

        private StatefulNode node;

        public Heartbeat(StatefulNode node) {
            this.node = node;
        }

        @Override
        public void run() {
            node.getClients().heartbeat();
        }

    }

    private class Election extends TimerTask {

        private StatefulNode node;

        public Election(StatefulNode node) {
            this.node = node;
        }

        @Override
        public void run() {
            node.conversionTo(NodeState.CANDIDATE);
        }

    }
}
