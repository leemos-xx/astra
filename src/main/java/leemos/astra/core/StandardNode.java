package leemos.astra.core;

import java.util.Random;

import leemos.astra.Constants;
import leemos.astra.Lifecycle;
import leemos.astra.Node;
import leemos.astra.NodeConfig;
import leemos.astra.Request;
import leemos.astra.Response;
import leemos.astra.core.election.ElectionManager;
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
    private ElectionManager electionManager;

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

        
    }

    /*
     * @see leemos.astra.Lifecycle#start()
     */
    @Override
    public void start() throws LifecycleException {
        
    }

    @Override
    public Response handleAppendEntries(Request appendEntries) {
        return null;
    }

    @Override
    public Response handleReuqestVote(Request requestVote) {
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
