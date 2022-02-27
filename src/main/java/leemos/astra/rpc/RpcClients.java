package leemos.astra.rpc;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leemos.astra.Clients;
import leemos.astra.LifecycleException;
import leemos.astra.Node;
import leemos.astra.rpc.scene.RaftScene;
import leemos.orion.client.Proxy;
import leemos.orion.client.ProxyInvoker;
import leemos.orion.client.proxy.JdkProxy;
import leemos.orion.client.proxy.JdkProxyInvoker;
import leemos.orion.client.rpc.OrionClient;

public class RpcClients implements Clients {

    private static final Logger logger = LoggerFactory.getLogger(Node.class);
    
    private String[] peers;
    private OrionClient[] clients;
    private ConcurrentHashMap<String, RaftScene> scenes = new ConcurrentHashMap<String, RaftScene>();

    public RpcClients(String[] peers) {
        this.peers = peers;
    }

    @Override
    public void start() throws LifecycleException {
        clients = new OrionClient[peers.length];

        for (int i = 0; i < peers.length; i++) {
            OrionClient client = new OrionClient();
            try {
                client.start();

                Proxy proxy = new JdkProxy();
                ProxyInvoker invoker = new JdkProxyInvoker(peers[i], client);
                RaftScene scene = proxy.getProxy(RaftScene.class, invoker);

                scenes.put(peers[i], scene);
                clients[i] = client;
            } catch (Exception e) {
                throw new LifecycleException(e);
            }
        }
    }

    @Override
    public void stop() throws LifecycleException {
        for (OrionClient client : clients) {
            if (client == null) {
                continue;
            }

            try {
                client.stop();
            } catch (Exception e) {
                throw new LifecycleException(e);
            }
        }
    }

    @Override
    public void heartbeat() {
        logger.info("send: heartbeat...");
        Iterator<RaftScene> iterator = scenes.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().heartbeat();
        }
    }

    @Override
    public void requestVote() {
        logger.info("send: request vote...");
        Iterator<RaftScene> iterator = scenes.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().requestVote();
        }
    }

}
