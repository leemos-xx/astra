package leemos.astra;

import leemos.astra.core.StandardNode;
import org.apache.commons.cli.*;

/**
 * Astra 入口程序
 *
 * @author lihao
 * @date 2022年2月23日
 * @version 1.0
 */
public class Astra {

    private static Options options = new Options();
    private static CommandLine commandLine;
    private Node node;

    public static void main(String[] args) throws LifecycleException, ParseException {
        CommandLineParser commandLineParser = new DefaultParser();
        options.addOption(Option.builder("p").required().longOpt("peers").type(String[].class).build());
        options.addOption(Option.builder("e").longOpt("election").type(int.class).build());
        options.addOption(Option.builder("h").longOpt("heartbeat").type(int.class).build());

        commandLine = commandLineParser.parse(options, args);

        new Astra().start();
    }

    public void start() throws LifecycleException {
        node = StandardNode.getInstance();
        node.start();
    }

    public void stop() throws LifecycleException {
        if (node != null) {
            node.stop();
        }
    }
}