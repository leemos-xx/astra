package leemos.astra.core;

import leemos.astra.Clients;
import leemos.astra.Lifecycle;
import leemos.astra.LifecycleException;
import leemos.astra.Node;
import leemos.astra.NodeConfig;
import leemos.astra.Request;
import leemos.astra.Response;
import leemos.astra.Server;

/**
 * StandardNode {@link Node}的标准实现
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public class StandardNode extends AbstractNode implements Lifecycle {

    private NodeConfig config;
    private Server server;
    private Clients client;

    public StandardNode(NodeConfig config) {
        this.config = config;
    }

    @Override
    public NodeConfig getConfig() {
        return config;
    }

    @Override
    public Clients getClient() {
        return client;
    }

    @Override
    public void start() throws LifecycleException {
        server.start();
        client.start();

        transitionTo(NodeState.FOLLOWER);
    }

    @Override
    public void stop() throws LifecycleException {
        client.stop();
        server.stop();
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
