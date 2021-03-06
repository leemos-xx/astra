package leemos.astra;

/**
 * Log 日志模块
 * 
 * @author lihao
 * @date 2022-02-27
 * @version 1.0
 */
public interface Log {

    /**
     * 是否包含该索引位的日志
     *
     * @param logIndex
     * @return
     */
    public boolean contains(long logIndex);

    /**
     * 写日志，追加到最末
     * 
     * @param logEntry
     */
    public void write(LogEntry logEntry);

    /**
     * 从指定索引位置读取日志
     * 
     * @param logIndex
     * @return
     */
    public LogEntry read(long logIndex);
    
    /**
     * 获取最新的日志
     * @return
     */
    public LogEntry last();

    /**
     * 从指定索引位置开始截断日志
     * 
     * @param logIndex
     */
    public void truncate(long logIndex);
}
