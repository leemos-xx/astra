package leemos.astra.rpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leemos.astra.rpc.AppendEntriesReq;
import leemos.astra.rpc.AppendEntriesResp;
import leemos.astra.rpc.RaftScene;
import leemos.astra.rpc.RequestVoteReq;
import leemos.astra.rpc.RequestVoteResp;
import leemos.orion.core.engine.Api;
import leemos.orion.core.engine.Scene;

@Scene(name = "raft")
public class RaftSceneImpl implements RaftScene {

    private static final Logger logger = LoggerFactory.getLogger(RaftSceneImpl.class);

    @Api(name = "requestVote")
    @Override
    public RequestVoteResp requestVote(RequestVoteReq request) {
        logger.info("recv: requestVote...");
        return null;
    }

    @Api(name = "appendEntries`")
    @Override
    public AppendEntriesResp appendEntries(AppendEntriesReq request) {
        logger.info("recv: appendEntries...");
        return null;
    }

    @Api(name = "heartbeat")
    @Override
    public AppendEntriesResp heartbeat(AppendEntriesReq request) {
        logger.info("recv: heartbeat...");
        return null;
    }

}
