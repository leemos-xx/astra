package leemos.astra;

import leemos.astra.rpc.AppendEntriesReq;
import leemos.astra.rpc.AppendEntriesResp;
import leemos.astra.rpc.RequestVoteReq;
import leemos.astra.rpc.RequestVoteResp;

/**
 * Clients
 * 
 * @author lihao
 * @date 2022-03-05
 * @version 1.0
 */
public interface Client extends Lifecycle {

    /**
     * 请求投票
     */
    RequestVoteResp requestVote(RequestVoteReq request);

    /**
     * 心跳
     */
    AppendEntriesResp heartbeat(AppendEntriesReq request);

    /**
     * 附加日志
     */
    AppendEntriesResp appendEntries(AppendEntriesReq request);

}
