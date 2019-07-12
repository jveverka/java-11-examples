package itx.examples.akka.cluster.sshsessions.dto;

import java.io.Serializable;

/**
 * Created by juraj on 3/18/17.
 */
public class SessionError implements Serializable {

    private final String clientId;
    private final String error;

    public SessionError(String clientId, String error) {
        this.clientId = clientId;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public String getClientId() {
        return clientId;
    }
}
