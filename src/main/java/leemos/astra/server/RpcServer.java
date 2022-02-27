package leemos.astra.server;

import java.util.concurrent.CountDownLatch;

import leemos.astra.LifecycleException;
import leemos.astra.Server;
import leemos.orion.core.StandardServer;

public class RpcServer implements Server {
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
        } catch (Exception e) {
            throw new LifecycleException(e);
        }
    }
}
