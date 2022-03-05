package leemos.astra.rpc;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * RequestVoteResp
 * 
 * @author lihao
 * @date 2022-03-05
 * @version 1.0
 */
@Setter
@Getter
@Builder
public class RequestVoteResp {
    private int term;
    private boolean voteGranted;
}
