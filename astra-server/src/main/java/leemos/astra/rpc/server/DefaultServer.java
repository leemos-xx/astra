package leemos.astra.rpc.server;

import java.util.concurrent.CountDownLatch;

import leemos.astra.LifecycleException;
import leemos.astra.Server;
import leemos.orion.core.StandardServer;

/**
 * RpcServer 
 * 
 * @author lihao
 * @date 2022-03-05
 * @version 1.0
 */
public class DefaultServer implements Server {
    private StandardServer server = new StandardServer();
    private CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void start() throws LifecycleException {
        try {
            server.start();
            latch.await();
        } catch (Exception e) {
            throw new LifecycleException(e);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        try {
            server.stop();
            latch.countDown();
        } catch (Exception e) {
            throw new LifecycleException(e);
        }
    }
}
