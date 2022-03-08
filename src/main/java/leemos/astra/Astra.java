package leemos.astra;

import leemos.astra.core.StandardNode;

/**
 * Astra 入口程序
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public class Astra {

    private Node node;

    public static void main(String[] args) throws LifecycleException {
        new Astra().start();
    }

    public void start() throws LifecycleException {
        node = StandardNode.getInstance();
        node.start();
    }

    public void stop() throws LifecycleException {
        if (node != null) {
            node.stop();
        }
    }
}