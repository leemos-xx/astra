package leemos.astra;

import leemos.astra.core.StandardNode;
import org.apache.commons.cli.*;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Astra 入口程序
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public class Astra {

    private final static Options options = new Options();
    private static CommandLine commandLine;
    private Node node;

    public static void main(String[] args) throws LifecycleException, ParseException, UnknownHostException {
        CommandLineParser commandLineParser = new DefaultParser();
        options.addOption(Option.builder("p").hasArg(true).required().longOpt("peers").type(String[].class).build());

        commandLine = commandLineParser.parse(options, args);

        new Astra().start();
    }

    public void start() throws LifecycleException, UnknownHostException {
        NodeConfig config = NodeConfig.builder()
                // FIXME host address
                .id(Inet4Address.getLocalHost().getHostAddress())
                .peers(commandLine.getOptionValues("p"))
                .electionTimeout(10000)
                .heartbeatTimeout(3000)
                .build();

        node = new StandardNode(config);
        node.start();
    }

    public void stop() throws LifecycleException {
        if (node != null) {
            node.stop();
        }
    }
}