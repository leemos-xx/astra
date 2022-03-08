package leemos.astra;

import leemos.astra.node.NodeState;

/**
 * Node 代表集群中的任意一个节点，该接口中声明了这些节点的通用行为
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public interface Node extends Lifecycle {

    /**
     * 获取节点Id
     * @return
     */
    String getId();
    
    /**
     * 获取Node节点的配置
     * 
     * @return
     */
    NodeConfig getConfig();
    
    /**
     * 获取所有对等节点
     * @return
     */
    Client[] getClients();
    
    /**
     * 获取日志模块
     * @return
     */
    Log getLog();
    
    /**
     * 获取一致性模块
     * @return
     */
    Consensus getConsensus();
    
    /**
     * 获取状态机模块
     * @return
     */
    StateMachine getStateMachine();

    /**
     * 切换节点状态
     * @param newState
     */
    void conversionTo(NodeState newState);
}
