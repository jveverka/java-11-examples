package itx.examples.akka.cluster.sshsessions.dto;

import java.io.Serializable;

/**
 * Created by gergej on 25.3.2017.
 */
public class ActiveSession implements Serializable {

    private final String sessionId;
    private final String clientId;
    private final String clientSessionActorAddress;

    public ActiveSession(String sessionId, String clientId, String clientSessionActorAddress) {
        this.sessionId = sessionId;
        this.clientId = clientId;
        this.clientSessionActorAddress = clientSessionActorAddress;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSessionActorAddress() {
        return clientSessionActorAddress;
    }
}
