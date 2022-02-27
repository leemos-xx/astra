package leemos.astra;

public interface Client extends Lifecycle {

    void heartbeat();

    void requestVote();

}
