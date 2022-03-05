package leemos.astra;

import leemos.astra.node.StandardNode;
import leemos.astra.rpc.clients.StandardClient;
import leemos.astra.rpc.server.StandardServer;

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
    private StandardServer server;
    private StandardClient clients;

    public Astra(NodeConfig config) {
        this.config = config;
    }

    public static void main(String[] args) throws LifecycleException {
        new Astra(NodeConfig.builder().peers(new String[] { "localhost:10880" }).electionTimeout(10000)
                .heartbeatTimeout(30000).build()).start();
    }

    public void start() throws LifecycleException {
        server = new StandardServer();
        clients = new StandardClient(config.getPeers());

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