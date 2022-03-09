package leemos.astra.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import leemos.astra.event.Event;
import leemos.astra.event.EventBus;
import leemos.astra.event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leemos.astra.Client;
import leemos.astra.LifecycleException;
import leemos.astra.Node;
import leemos.astra.NodeConfig;
import leemos.astra.Server;
import leemos.astra.rpc.clients.DefaultClient;
import leemos.astra.rpc.server.DefaultServer;

/**
 * StandardNode {@link Node}的标准实现，仅负责通讯相关实现
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public class StandardNode extends StatefulNode {

    private static final Logger logger = LoggerFactory.getLogger(StandardNode.class);

    private Server server;
    private Client[] clients;
    private ExecutorService executor;

    public StandardNode(NodeConfig config) {
        super(config);
    }

    @Override
    public String getId() {
        return config.getId();
    }

    @Override
    public void start() throws LifecycleException {
        // 启动server
        startServer();

        // 建立与其他节点的连接
        startClients();

        // 初始化为Follower
        EventBus.get().fireEvent(new Event(EventType.CONVERSION_TO_FOLLOWER));
    }

    private void startServer() {
        executor = Executors.newFixedThreadPool(1);
        executor.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    server = new DefaultServer();
                    server.start();
                } catch (LifecycleException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }

    private void startClients() throws LifecycleException {
        clients = new Client[config.getPeers().length];
        for (int i = 0; i < clients.length; i++) {
            clients[i] = new DefaultClient(config.getPeers()[i]);
            clients[i].start();
        }
    }

    @Override
    public void stop() throws LifecycleException {
        for (Client client : clients) {
            client.stop();
        }
        server.stop();
    }

    @Override
    public Client[] getClients() {
        return clients;
    }
}
