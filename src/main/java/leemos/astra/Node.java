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
     * 处理附加日志的请求
     *
     * @param appendEntriesRequest
     * @return
     */
    Response handleAppendEntriesRequest(Request appendEntriesRequest);

    /**
     * 处理投票请求
     *
     * @param voteRequest
     * @return
     */
    Response handleVoteReuqest(Request voteRequest);

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

    /**
     * 枚举Node可能存在的状态
     *
     * @author lihao
     * @date 2022年2月23日
     * @version 1.0
     */
    public static enum State {
        /**
         * 跟随者
         */
        FOLLOWER(0),
        /**
         * 候选者
         */
        CANDIDATE(1),
        /**
         * 领导者
         */
        LEADER(2),
        /**
         * 未知，不会出现此种状态
         */
        UNKNOWN(-1);

        int status;

        State(int status) {
            this.status = status;
        }

        public State parse(int status) {
            for (State value : State.values()) {
                if (value.status == status) {
                    return value;
                }
            }
            return UNKNOWN;
        }
    }
}
