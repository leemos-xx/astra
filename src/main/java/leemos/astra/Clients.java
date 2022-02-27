package leemos.astra;

public interface Clients extends Lifecycle {

    void heartbeat();

    void requestVote();

}
