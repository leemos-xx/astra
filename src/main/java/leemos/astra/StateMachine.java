package leemos.astra;

/**
 * StateMachine 状态机
 * 
 * @author lihao
 * @date 2022-02-27
 * @version 1.0
 */
public interface StateMachine {

    /**
     * 应用日志，更新状态机数据
     * 
     * @param logEntry
     */
    public void apply(LogEntry logEntry);

    /**
     * 从状态机中读取数据
     * 
     * @param <T>
     * @param key
     * @return
     */
    public <T> T get(String key);
}
