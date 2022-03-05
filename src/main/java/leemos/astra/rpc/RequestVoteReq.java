package leemos.astra.rpc;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * RequestVoteReq
 * 
 * @author lihao
 * @date 2022-03-05
 * @version 1.0
 */
@Setter
@Getter
@Builder
public class RequestVoteReq {
    private int term;
    private String candidateId;
    private long lastLogIndex;
    private long lastLogTerm;
}
