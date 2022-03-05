package leemos.astra.rpc.server;

import leemos.astra.Consensus;
import leemos.astra.Log;
import leemos.astra.LogEntry;
import leemos.astra.node.StandardNode;
import leemos.astra.rpc.AppendEntriesReq;
import leemos.astra.rpc.AppendEntriesResp;
import leemos.astra.rpc.RaftScene;
import leemos.astra.rpc.RequestVoteReq;
import leemos.astra.rpc.RequestVoteResp;
import leemos.orion.core.engine.Api;
import leemos.orion.core.engine.Scene;

@Scene(name = "raft")
public class RaftSceneImpl implements RaftScene {

    @Api(name = "requestVote")
    @Override
    public synchronized RequestVoteResp requestVote(RequestVoteReq request) {
        Consensus consensus = StandardNode.getInstance().getConsensus();
        Log log = StandardNode.getInstance().getLog();

        // 如果已经为其他人投过票，则不再投票
        if (consensus.voting()) {
            return RequestVoteResp.builder().term(consensus.getCurrentTerm()).voteGranted(false).build();
        }

        // 如果请求中的任期比当前节点任期小，则投反对票
        if (request.getTerm() < consensus.getCurrentTerm()) {
            return RequestVoteResp.builder().term(consensus.getCurrentTerm()).voteGranted(false).build();
        }

        // FIXME 整理逻辑：如果请求中的日志比当前节点旧，则投反对票
        if (request.getLastLogIndex() < log.last().getLogIndex() || request.getLastLogTerm() < log.last().getTerm()) {
            return RequestVoteResp.builder().term(consensus.getCurrentTerm()).voteGranted(false).build();
        }

        consensus.voteFor(request.getCandidateId());

        return RequestVoteResp.builder().term(consensus.getCurrentTerm()).voteGranted(true).build();
    }

    @Api(name = "appendEntries")
    @Override
    public AppendEntriesResp appendEntries(AppendEntriesReq request) {
        Consensus consensus = StandardNode.getInstance().getConsensus();
        Log log = StandardNode.getInstance().getLog();
        
        // 如果心跳中Leader的任期比当前节点小，则通知Leader退位为Follower
        if (request.getTerm() < consensus.getCurrentTerm()) {
            return AppendEntriesResp.builder().term(consensus.getCurrentTerm()).success(false).build();
        }
        
        // 如果请求中的日志比当前节点旧，则通过Leader进行日志复制
        if (request.getPrevLogIndex() < log.last().getLogIndex() || request.getPrevLogTerm() < log.last().getTerm()) {
            return AppendEntriesResp.builder().term(consensus.getCurrentTerm()).success(false).build();
        }
        
        LogEntry entry = log.read(request.getPrevLogIndex());
        if (entry == null || entry.getTerm() != request.getPrevLogTerm()) {
            return AppendEntriesResp.builder().term(consensus.getCurrentTerm()).success(false).build();
        }

        return AppendEntriesResp.builder().term(consensus.getCurrentTerm()).success(true).build();
    }

    @Api(name = "heartbeat")
    @Override
    public AppendEntriesResp heartbeat(AppendEntriesReq request) {
        // FIXME
        return null;
    }

}
