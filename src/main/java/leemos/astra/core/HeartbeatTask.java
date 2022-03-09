package leemos.astra.core;

import leemos.astra.Consensus;
import leemos.astra.LogEntry;
import leemos.astra.event.Event;
import leemos.astra.event.EventBus;
import leemos.astra.event.EventType;
import leemos.astra.rpc.AppendEntriesReq;
import leemos.astra.rpc.AppendEntriesResp;

import java.util.TimerTask;

public class HeartbeatTask extends TimerTask {

    private StatefulNode node;

    public HeartbeatTask(StatefulNode node) {
        this.node = node;
    }

    @Override
    public void run() {
        outter: for (int i = 0; i < node.getClients().length; i++) {
            long logIndex = StandardLog.get().last().getLogIndex();

            while (true) {
                LogEntry entry = StandardLog.get().read(logIndex);

                AppendEntriesReq request = AppendEntriesReq.builder()
                        .term(Consensus.get().getCurrentTerm())
                        .leaderId(node.getId())
                        .prevLogIndex(entry.getLogIndex())
                        .prevLogTerm(entry.getTerm())
                        .entries(new LogEntry[0])
                        .leaderCommit(Consensus.get().getCommitIndex())
                        .build();
                AppendEntriesResp response = node.getClients()[i].heartbeat(request);

                if (response.getTerm() > Consensus.get().getCurrentTerm()) {
                    EventBus.get().fireEvent(new Event(EventType.CONVERSION_TO_FOLLOWER));
                    break outter;
                }

                if (response.isSuccess()) {
                    Consensus.get().updateCommit(i, Consensus.get().getCommitIndex());
                    break;
                } else {
                    logIndex--;
                    Consensus.get().updateMatch(i, logIndex);
                }
            }
        }
    }
}
