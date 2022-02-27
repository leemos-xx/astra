package leemos.astra.rpc.scene;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leemos.orion.core.engine.Api;
import leemos.orion.core.engine.Scene;

@Scene(name = "raft")
public class RaftSceneImpl implements RaftScene {

    private static final Logger logger = LoggerFactory.getLogger(RaftSceneImpl.class);
    
    @Api(name = "heartbeat")
    @Override
    public void heartbeat() {
        logger.info("recv: heartbeat...");
    }

    @Api(name = "requestVote")
    @Override
    public void requestVote() {
        logger.info("recv: requestVote...");
    }

}
