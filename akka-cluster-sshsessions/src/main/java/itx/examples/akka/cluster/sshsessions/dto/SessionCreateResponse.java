package itx.examples.akka.cluster.sshsessions.dto;

import java.io.Serializable;

/**
 * Created by juraj on 3/18/17.
 */
public class SessionCreateResponse implements Serializable {

    private String sessionId;
    private String clientId;
    private String sessionActorAddress;
    private String sshLocalSessionManagerActorAddress;

    public SessionCreateResponse(String sessionId, String clientId,
                                 String sessionActorAddress, String sshLocalSessionManagerActorAddress) {
        this.sessionId = sessionId;
        this.clientId = clientId;
        this.sessionActorAddress = sessionActorAddress;
        this.sshLocalSessionManagerActorAddress = sshLocalSessionManagerActorAddress;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSessionActorAddress() {
        return sessionActorAddress;
    }

    public String getSshLocalSessionManagerActorAddress() {
        return sshLocalSessionManagerActorAddress;
    }

}
