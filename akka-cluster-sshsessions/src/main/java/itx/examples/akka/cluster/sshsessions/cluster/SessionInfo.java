package itx.examples.akka.cluster.sshsessions.cluster;

/**
 * Created by juraj on 3/24/17.
 */
public class SessionInfo {

    private String clientSessionActorAddress;
    private String clientId;
    private String sshLocalSessionManagerActorAddress;
    private String sshSessionId;

    public SessionInfo(String clientSessionActorAddress, String clientId,
                       String sshLocalSessionManagerActorAddress, String sshSessionId) {
        this.clientSessionActorAddress = clientSessionActorAddress;
        this.clientId = clientId;
        this.sshLocalSessionManagerActorAddress = sshLocalSessionManagerActorAddress;
        this.sshSessionId = sshSessionId;
    }

    public String getClientSessionActorAddress() {
        return clientSessionActorAddress;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSshSessionId() {
        return sshSessionId;
    }

    public String getSshLocalSessionManagerActorAddress() {
        return sshLocalSessionManagerActorAddress;
    }

}
