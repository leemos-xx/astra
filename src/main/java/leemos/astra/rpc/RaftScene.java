package leemos.astra.rpc;

import leemos.orion.client.ServiceInvocation;

public interface RaftScene {

    @ServiceInvocation(name = "raft.requestVote")
    public RequestVoteResp requestVote(RequestVoteReq request);

    @ServiceInvocation(name = "raft.appendEntries")
    public AppendEntriesResp appendEntries(AppendEntriesReq request);

    @ServiceInvocation(name = "raft.heartbeat")
    public AppendEntriesResp heartbeat(AppendEntriesReq request);
}
