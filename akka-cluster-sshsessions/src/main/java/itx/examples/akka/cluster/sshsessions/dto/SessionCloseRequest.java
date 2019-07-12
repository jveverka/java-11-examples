package itx.examples.akka.cluster.sshsessions.dto;

import java.io.Serializable;

/**
 * Created by juraj on 3/18/17.
 */
public class SessionCloseRequest implements Serializable {

    private final String clientId;
    private final String sshSessionId;
    private final String sessionActorAddress;

    public SessionCloseRequest(String clientId, String sshSessionId, String sessionActorAddress) {
        this.clientId = clientId;
        this.sshSessionId = sshSessionId;
        this.sessionActorAddress = sessionActorAddress;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSshSessionId() {
        return sshSessionId;
    }

    public String getSessionActorAddress() {
        return sessionActorAddress;
    }

}
