package leemos.astra;

/**
 * Node 代表集群中的任意一个节点，该接口中声明了这些节点的通用行为
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public interface Node {

    /**
     * 获取Node节点的配置
     * 
     * @return
     */
    NodeConfig getConfig();

    /**
     * 获取客户端
     * 
     * @return
     */
    Client getClient();

    /**
     * 处理附加日志的请求
     *
     * @param appendEntries
     * @return
     */
    Response handleAppendEntries(Request appendEntries);

    /**
     * 处理投票请求
     *
     * @param requestVote
     * @return
     */
    Response handleReuqestVote(Request requestVote);

    /**
     * 处理客户端请求，仅处在Status.LEADER状态时
     *
     * @param clientRequest
     * @return
     */
    Response handleClientRequest(Request clientRequest);

    /**
     * 将客户端请求重定向到Leader，处在Status.FOLLOWER & Status.CANDIDATE状态时
     *
     * @param clientRequest
     */
    void redirectClientRequest(Request clientRequest);

}
