package leemos.astra;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NodeConfig {

    private String[] peers;

    private int electionTimeout;
    private int heartbeatTimeout;

}
