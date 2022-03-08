package leemos.astra.rpc;

import java.io.Serializable;

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
public class RequestVoteReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private int term;
    private String candidateId;
    private long lastLogIndex;
    private long lastLogTerm;
}
