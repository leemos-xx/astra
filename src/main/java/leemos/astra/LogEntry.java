package leemos.astra;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * LogEntry 日志复制中的条目
 * 
 * @author lihao
 * @date 2022-02-27
 * @version 1.0
 */
@Getter
@Setter
@Builder
public class LogEntry {

    /**
     * 日志索引
     */
    private long logIndex;

    /**
     * Leader的任期
     */
    private int term;

    /**
     * 日志中包含的指令
     */
    private Instruction<?> instruction;
}
