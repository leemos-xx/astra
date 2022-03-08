package leemos.astra;

/**
 * Consensus 用于实现分布式共识
 * 
 * @author lihao
 * @date 2022-02-27
 * @version 1.0
 */
public class Consensus {

    private int currentTerm;
    private String voteFor;
    private long commitIndex;
    private long lastApplied;
    private long[] nextIndex;
    private long[] matchIndex;

    public Consensus(int peers) {
        this.nextIndex = new long[peers];
        this.matchIndex = new long[peers];
    }

    public void increaseTeam() {
        this.currentTerm++;
    }

    public void decreaseTerm() {
        this.currentTerm--;
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
