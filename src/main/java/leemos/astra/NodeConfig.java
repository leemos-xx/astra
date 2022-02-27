package leemos.astra;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class NodeConfig {

    private String[] peers;

    private int electionTimeout;
    private int heartbeatTimeout;

}
