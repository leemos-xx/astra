package leemos.astra;

/**
 * Node 代表集群中的任意一个节点，该接口中声明了这些节点的通用行为
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public interface Node extends Lifecycle {

    /**
     * 获取Node节点的配置
     * 
     * @return
     */
    NodeConfig getConfig();
}
