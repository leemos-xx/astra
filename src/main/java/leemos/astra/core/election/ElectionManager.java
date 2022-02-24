package leemos.astra.core.election;

import java.util.Random;

import leemos.astra.Constants;
import leemos.astra.Node;

/**
 * ElectionManager 用于协助{@link Node}竞选Leader
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public class ElectionManager {

    private int heartbeatTimeout;
    private int electionTimeout;

    public ElectionManager() {
        // 通过这两个超时时间来控制election。若当前node的state不是leader，则
        // 它会每隔heartbeatTimeout毫秒收到leader发送来的心跳消息，证明leader
        // 目前是存活可用的状态。但如果经过electionTimeout后仍然未收到leader发来
        // 的心跳消息（electionTimeout >> heartbeatTimeout），则当前node将
        // 成为candidate，并请求集群内其它节点的投票，如果票选通过，则成为新的leader。
        this.heartbeatTimeout = Constants.HEARTBEAT_TIMEOUT;
        // electionTimeout在150ms-300ms之间
        this.electionTimeout = Constants.ELECTION_TIMEOUT_ORIGIN
                + new Random().nextInt(Constants.ELECTION_TIMEOUT_BOUND);
    }
}
