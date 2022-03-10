package leemos.astra;

import leemos.astra.event.EventBus;
import leemos.astra.event.EventListener;

import java.util.ArrayList;

/**
 * Consensus 用于实现分布式共识
 * 
 * @author lihao
 * @date 2022-02-27
 * @version 1.0
 */
public class Consensus {

    private static volatile Consensus singleton;

    private int currentTerm = 0;
    private String voteFor = null;
    private long commitIndex = -1;
    private long lastApplied = -1;
    private long[] nextIndex;
    private long[] matchIndex;

    public static void init(int peers) {
        if (singleton == null) {
            synchronized (Consensus.class) {
                if (singleton == null) {
                    singleton = new Consensus(peers);
                }
            }
        }
    }

    public static Consensus get() {
        return singleton;
    }

    private Consensus(int peers) {
        this.nextIndex = new long[peers];
        this.matchIndex = new long[peers];
    }

    public void increaseTeam() {
        this.currentTerm++;
    }

    public void decreaseTerm() {
        this.currentTerm--;
    }

    public void setCurrentTerm(int currentTerm) {
        this.currentTerm = currentTerm;
    }

    public int getCurrentTerm() {
        return currentTerm;
    }

    public void voteFor(String candidateId) {
        this.voteFor = candidateId;
    }

    public boolean voting() {
        return this.voteFor == null;
    }

    public void setCommitIndex(long commitIndex) {
        this.commitIndex = commitIndex;
    }

    public long getCommitIndex() {
        return commitIndex;
    }

    public void setLastApplied(long lastApplied) {
        this.lastApplied = lastApplied;
    }

    public long getLastApplied() {
        return lastApplied;
    }

    public void updateCommit(int idx, long commit) {
        this.nextIndex[idx] = commit;
    }

    public void updateMatch(int idx, long match) {
        this.matchIndex[idx] = match;
    }
}
