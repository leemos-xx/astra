package leemos.astra.rpc.scene;

import leemos.orion.client.ServiceInvocation;

public interface RaftScene {

    @ServiceInvocation(name = "raft.heartbeat")
    public void heartbeat();
    
    @ServiceInvocation(name = "raft.requestVote")
    public void requestVote();
}
