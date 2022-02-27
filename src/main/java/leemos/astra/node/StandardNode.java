package leemos.astra.node;

import leemos.astra.Clients;
import leemos.astra.LifecycleException;
import leemos.astra.Node;
import leemos.astra.NodeConfig;
import leemos.astra.Request;
import leemos.astra.Response;
import leemos.astra.Server;
import leemos.astra.rpc.RpcClients;
import leemos.astra.rpc.RpcServer;

/**
 * StandardNode {@link Node}的标准实现
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public class StandardNode extends AbstractNode {

    private NodeConfig config;
    private Server server;
    private Clients clients;

    public StandardNode(NodeConfig config, RpcServer server, RpcClients clients) {
        this.config = config;
        this.server = server;
        this.clients = clients;
    }

    @Override
    public NodeConfig getConfig() {
        return config;
    }

    @Override
    public Clients getClients() {
        return clients;
    }

    @Override
    public void start() throws LifecycleException {
        // FIXME 用线程池，或命名
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.start();
                } catch (LifecycleException e) {
                }
            }
        }).start();
        
        clients.start();

        transitionTo(NodeState.FOLLOWER);
    }

    @Override
    public void stop() throws LifecycleException {
        clients.stop();
        server.stop();
    }

    @Override
    public Response handleAppendEntries(Request appendEntries) {
        logger.info("recv: append entries...");
        return null;
    }

    @Override
    public Response handleReuqestVote(Request requestVote) {
        logger.info("recv: request vote...");
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
