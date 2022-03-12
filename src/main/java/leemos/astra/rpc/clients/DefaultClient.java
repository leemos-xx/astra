package leemos.astra.rpc.clients;

import leemos.astra.Client;
import leemos.astra.LifecycleException;
import leemos.astra.rpc.AppendEntriesReq;
import leemos.astra.rpc.AppendEntriesResp;
import leemos.astra.rpc.RaftScene;
import leemos.astra.rpc.RequestVoteReq;
import leemos.astra.rpc.RequestVoteResp;
import leemos.orion.client.proxy.ClientProxy;
import leemos.orion.client.rpc.OrionClient;

/**
 * EventListener
 *
 * @author lihao
 * @date 2022-03-12
 * @version 1.0
 */
public class DefaultClient implements Client {

    private String peer;
    private OrionClient client;
    private RaftScene scene;

    public DefaultClient(String peer) {
        this.peer = peer;
    }

    @Override
    public RequestVoteResp requestVote(RequestVoteReq request) {
        return this.scene.requestVote(request);
    }

    @Override
    public AppendEntriesResp heartbeat(AppendEntriesReq request) {
        return this.scene.heartbeat(request);
    }

    @Override
    public AppendEntriesResp appendEntries(AppendEntriesReq request) {
        return this.scene.appendEntries(request);
    }

    @Override
    public void start() throws LifecycleException {
        OrionClient client = new OrionClient();
        try {
            client.start();

            RaftScene scene = ClientProxy.getProxy(peer, RaftScene.class, client);

            this.scene = scene;
            this.client = client;
        } catch (Exception e) {
            throw new LifecycleException(e);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        if (this.client == null) {
            return;
        }

        try {
            client.stop();
        } catch (Exception e) {
            throw new LifecycleException(e);
        }
    }

}
