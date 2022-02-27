package leemos.astra.core;

import java.util.Timer;
import java.util.TimerTask;

import leemos.astra.Node;

/**
 * AbstractNode 用于控制Node的状态切换
 * 
 * @author lihao
 * @date 2022-02-27
 * @version 1.0
 */
public abstract class AbstractNode implements Node {

    private NodeState state = NodeState.FOLLOWER;
    private Timer electionTimer = new Timer();
    private Timer heartbeatTimer = new Timer();

    protected synchronized void transitionTo(NodeState newState) {
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

        switch (state) {
        case FOLLOWER:
            transitionToFollower();

            break;
        case CANDIDATE:
            transitionToCandidate();

            break;
        case LEADER:
            transitionToLeader();

            break;
        default:

            break;
        }

        this.state = newState;
    }

    private void transitionToLeader() {
        int heartbeatTimeout = getConfig().getHeartbeatTimeout();
        heartbeatTimer.scheduleAtFixedRate(new Heartbeat(this), 0, heartbeatTimeout);
    }

    private void resignFromLeader() {
        heartbeatTimer.cancel();
    }

    private void transitionToCandidate() {
        getClient().requestVote();
    }

    // FIXME 是否需要此方法
    private void resignFromCandidate() {

    }

    private void transitionToFollower() {
        int electionTimeout = getConfig().getElectionTimeout();
        electionTimer.scheduleAtFixedRate(new Election(this), electionTimeout, electionTimeout);
    }

    private void resignFromFollower() {
        electionTimer.cancel();
    }

    private class Heartbeat extends TimerTask {

        private AbstractNode node;

        public Heartbeat(AbstractNode node) {
            this.node = node;
        }

        @Override
        public void run() {
            node.getClient().heartbeat();
        }

    }

    private class Election extends TimerTask {

        private AbstractNode node;

        public Election(AbstractNode node) {
            this.node = node;
        }

        @Override
        public void run() {
            node.transitionTo(NodeState.CANDIDATE);
        }

    }
}
