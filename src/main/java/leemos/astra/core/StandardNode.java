package leemos.astra.core;

import java.util.Random;

import leemos.astra.Constants;
import leemos.astra.Lifecycle;
import leemos.astra.Node;
import leemos.astra.NodeConfig;
import leemos.astra.Request;
import leemos.astra.Response;
import leemos.astra.exception.LifecycleException;

/**
 * StandardNode {@link Node}的标准实现
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public class StandardNode implements Node, Lifecycle {

    private NodeConfig config;
    private State state;
    private int heartbeatTimeout;
    private int electionTimeout;

    public StandardNode(NodeConfig config) {
        this.config = config;
    }

    /*
     * @see leemos.astra.Lifecycle#init()
     */
    @Override
    public void init() throws LifecycleException {
        // Node初始化时，均为FOLLOWER状态
        this.state = Node.State.FOLLOWER;

        // 通过这两个超时时间来控制election。若当前node的state不是leader，则
        // 它会每隔heartbeatTimeout毫秒收到leader发送来的心跳消息，证明leader
        // 目前是存活可用的状态。但如果经过electionTimeout后仍然未收到leader发来
        // 的心跳消息（electionTimeout >> heartbeatTimeout），则当前node将
        // 成为candidate，并请求集群内其它节点的投票，如果票选通过，则成为新的leader。
        this.heartbeatTimeout = Constants.HEARTBEAT_TIMEOUT;
        // electionTimeout在150ms-300ms之间
        this.electionTimeout = Constants.ELECTION_TIMEOUT_ORIGIN
                + new Random().nextInt(Constants.ELECTION_TIMEOUT_BOUND);
    }

    @Override
    public Response handleAppendEntriesRequest(Request appendEntriesRequest) {
        return null;
    }

    @Override
    public Response handleVoteReuqest(Request voteRequest) {
        return null;
    }

    @Override
    public Response handleClientRequest(Request clientRequest) {
        return null;
    }

    @Override
    public void redirectClientRequest(Request clientRequest) {

    }

}
