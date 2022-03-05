package leemos.astra.rpc;

import leemos.astra.LogEntry;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * AppendEntriesReq
 * 
 * @author lihao
 * @date 2022-03-05
 * @version 1.0
 */
@Setter
@Getter
@Builder
public class AppendEntriesReq {
    private int term;
    private String leaderId;
    private long prevLogIndex;
    private int prevLogTerm;
    private LogEntry[] entries;
    private long leaderCommit;
}
