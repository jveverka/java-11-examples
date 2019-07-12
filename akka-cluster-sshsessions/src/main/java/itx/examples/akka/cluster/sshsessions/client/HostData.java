package itx.examples.akka.cluster.sshsessions.client;

import java.io.Serializable;

/**
 * Created by juraj on 25.3.2017.
 */
public class HostData implements Serializable {

    private final String hostName;
    private final int port;

    public HostData(String hostName, int port) {
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
