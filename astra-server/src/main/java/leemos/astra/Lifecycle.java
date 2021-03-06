package leemos.astra;

/**
 * 定义生命周期的各阶段
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public interface Lifecycle {

    /**
     * 启动阶段
     *
     * @throws LifecycleException
     */
    default void start() throws LifecycleException {
    }

    /**
     * 停止阶段
     *
     * @throws LifecycleException
     */
    default void stop() throws LifecycleException {
    }

}
