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
     * 从指定索引位置开始截断日志
     * 
     * @param logIndex
     */
    public void truncate(long logIndex);
}
