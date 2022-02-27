package leemos.astra;

import leemos.astra.rpc.RpcServer;

/**
 * Astra
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public class Astra {

    private NodeConfig config;

    public Astra(NodeConfig config) {
        this.config = config;
    }

    public static void main(String[] args) throws LifecycleException {
        new Astra(null).start();
    }
    
    public void start() throws LifecycleException {
        new RpcServer().start();
    }

    public void stop() {

    }

    public NodeConfig getConfig() {
        return config;
    }
}