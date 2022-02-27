package leemos.astra;

import leemos.astra.node.StandardNode;
import leemos.astra.rpc.RpcClients;
import leemos.astra.rpc.RpcServer;

/**
 * Astra 入口程序
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public class Astra {

    private NodeConfig config;
    private Node node;
    private RpcServer server;
    private RpcClients clients;

    public Astra(NodeConfig config) {
        this.config = config;
    }

    public static void main(String[] args) throws LifecycleException {
        new Astra(NodeConfig.builder().peers(new String[] { "localhost:10880" }).electionTimeout(10000)
                .heartbeatTimeout(30000).build()).start();
    }

    public void start() throws LifecycleException {
        server = new RpcServer();
        clients = new RpcClients(config.getPeers());

        node = new StandardNode(config, server, clients);

        node.start();
    }

    public void stop() throws LifecycleException {
        if (node != null) {
            node.stop();
        }
    }

    public NodeConfig getConfig() {
        return config;
    }
}