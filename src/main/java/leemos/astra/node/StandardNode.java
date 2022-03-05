package leemos.astra.node;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leemos.astra.Client;
import leemos.astra.LifecycleException;
import leemos.astra.Node;
import leemos.astra.NodeConfig;
import leemos.astra.Server;
import leemos.astra.rpc.clients.StandardClient;
import leemos.astra.rpc.server.StandardServer;

/**
 * StandardNode {@link Node}的标准实现
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public class StandardNode extends StatefulNode {

    private static final Logger logger = LoggerFactory.getLogger(StandardNode.class);

    private NodeConfig config;
    private Server server;
    private Client[] clients;
    private ExecutorService executor;

    public StandardNode(NodeConfig config) {
        this.config = config;
    }

    @Override
    public NodeConfig getConfig() {
        return config;
    }

    @Override
    public void start() throws LifecycleException {
        // 启动server
        executor = Executors.newFixedThreadPool(1);
        executor.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    server = new StandardServer();
                    server.start();
                } catch (LifecycleException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });

        // 建立与其他节点的连接
        clients = new Client[config.getPeers().length];
        for (int i = 0; i < clients.length; i++) {
            clients[i] = new StandardClient(config.getPeers()[i]);
            clients[i].start();
        }

        // 转换为Follower
        conversionTo(NodeState.FOLLOWER);
    }

    @Override
    public void stop() throws LifecycleException {
        for (Client client : clients) {
            client.stop();
        }
        server.stop();
    }

}
