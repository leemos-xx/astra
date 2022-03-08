package leemos.astra.rpc.server;

import java.util.concurrent.CountDownLatch;

import leemos.astra.LifecycleException;
import leemos.astra.Server;

/**
 * RpcServer 
 * 
 * @author lihao
 * @date 2022-03-05
 * @version 1.0
 */
public class StandardServer implements Server {
    private leemos.orion.core.StandardServer server = new leemos.orion.core.StandardServer();
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
