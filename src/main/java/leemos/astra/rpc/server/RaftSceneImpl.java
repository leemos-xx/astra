package leemos.astra.rpc.server;

import leemos.astra.Consensus;
import leemos.astra.LogEntry;
import leemos.astra.core.StandardLog;
import leemos.astra.core.StandardStateMachine;
import leemos.astra.event.Event;
import leemos.astra.event.EventBus;
import leemos.astra.event.EventType;
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

        EventBus.get().fireEvent(new Event(EventType.CONVERSION_TO_FOLLOWER));

        // 如果已经为其他人投过票，则不再投票
        if (!Consensus.get().voting()) {
            return generateRequestVoteResp(false);
        }

        // 如果请求中的任期比当前节点任期小，则投反对票
        if (request.getTerm() < Consensus.get().getCurrentTerm()) {
            return generateRequestVoteResp(false);
        }

        // 如果Candidate的日志比当前节点旧，则投反对票
        if (request.getLastLogTerm() < StandardLog.get().last().getTerm() || (request.getLastLogTerm() == StandardLog.get().last().getTerm()
                && request.getLastLogIndex() < StandardLog.get().last().getLogIndex())) {
            return generateRequestVoteResp(false);
        }

        Consensus.get().voteFor(request.getCandidateId());

        return generateRequestVoteResp(true);
    }

    private RequestVoteResp generateRequestVoteResp(boolean voteGranted) {
        return RequestVoteResp.builder().term(Consensus.get().getCurrentTerm()).voteGranted(voteGranted).build();
    }

    @Api(name = "appendEntries")
    @Override
    public AppendEntriesResp appendEntries(AppendEntriesReq request) {
        // 如果日志请求中Leader的任期比当前节点小，则通知Leader退位为Follower
        if (request.getTerm() < Consensus.get().getCurrentTerm()) {
            return generateAppendEntriesResp(false);
        }

        // 判断当前节点是否可以追加请求中的log entries，如果与leader预期的prevLogIndex&prevLogTerm不符，
        // 则返回追加日志失败
        LogEntry entry = StandardLog.get().read(request.getPrevLogIndex());
        if (entry == null) {
            return generateAppendEntriesResp(false);
        } else if (entry.getTerm() != request.getPrevLogTerm()){
            StandardLog.get().truncate(request.getPrevLogIndex());
            return generateAppendEntriesResp(false);
        }

        for (LogEntry e : request.getEntries()) {
            StandardLog.get().write(e);
        }

        // 如果leader的commitId比较新，则更新自己的commitId
        if (request.getLeaderCommit() > Consensus.get().getCommitIndex()) {
            commitLog(request.getLeaderCommit());
        }

        Consensus.get().setCurrentTerm(request.getTerm());

        return generateAppendEntriesResp(true);
    }

    @Api(name = "heartbeat")
    @Override
    public AppendEntriesResp heartbeat(AppendEntriesReq request) {
        // 如果心跳中Leader的任期比当前节点小，则通知Leader退位为Follower
        if (request.getTerm() < Consensus.get().getCurrentTerm()) {
            return generateAppendEntriesResp(false);
        }

        // 如果leader的commitId比较新，则更新自己的commitId
        if (request.getLeaderCommit() > Consensus.get().getCommitIndex()) {
            commitLog(request.getLeaderCommit());
        }

        EventBus.get().fireEvent(new Event(EventType.CONVERSION_TO_FOLLOWER));
        Consensus.get().setCurrentTerm(request.getTerm());

        return generateAppendEntriesResp(true);
    }

    private void commitLog(long commitIndex) {
        for (long logIndex = Consensus.get().getCommitIndex() + 1;
             logIndex <= commitIndex && logIndex <= StandardLog.get().last().getLogIndex(); logIndex++) {
            Consensus.get().setCommitIndex(logIndex);
            StandardStateMachine.get().apply(StandardLog.get().read(logIndex));
            Consensus.get().setLastApplied(logIndex);
        }
    }

    private AppendEntriesResp generateAppendEntriesResp(boolean success) {
        return AppendEntriesResp.builder().term(Consensus.get().getCurrentTerm()).success(success).build();
    }

}
