package leemos.astra.core;

/**
 * 枚举Node可能存在的状态
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public enum NodeState {
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

    NodeState(int status) {
        this.status = status;
    }

    public NodeState parse(int status) {
        for (NodeState value : NodeState.values()) {
            if (value.status == status) {
                return value;
            }
        }
        return UNKNOWN;
    }
}