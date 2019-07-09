package itx.examples.akka.cluster.sshsessions.tests.utils;

/**
 * Created by juraj on 25.3.2017.
 */
public class ClusterNode {

    private String hostName;
    private int port;

    public ClusterNode(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

}
