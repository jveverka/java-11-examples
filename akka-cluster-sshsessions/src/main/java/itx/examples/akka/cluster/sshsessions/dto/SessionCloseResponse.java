package itx.examples.akka.cluster.sshsessions.dto;

import java.io.Serializable;

/**
 * Created by juraj on 3/18/17.
 */
public class SessionCloseResponse implements Serializable {

    private String sessionId;
    private String clientId;
    private String clientActorAddress;

    public SessionCloseResponse(String sessionId, String clientId, String clientActorAddress) {
        this.sessionId = sessionId;
        this.clientId = clientId;
        this.clientActorAddress = clientActorAddress;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getClientActorAddress() {
        return clientActorAddress;
    }

    public String getClientId() {
        return clientId;
    }
}
