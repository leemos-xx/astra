package leemos.astra;

/**
 * 声明静态不会修改的配置项
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public interface Constants {

    /**
     * 心跳周期，单位ms
     */
    public static final int HEARTBEAT_TIMEOUT = 15;
    /**
     * 选举周期，单位ms
     */
    public static final int ELECTION_TIMEOUT_ORIGIN = 150;
    public static final int ELECTION_TIMEOUT_BOUND = 150;
}
