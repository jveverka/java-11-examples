package itx.examples.akka.cluster.sshsessions.dto;

import itx.examples.akka.cluster.sshsessions.client.HostData;
import itx.examples.akka.cluster.sshsessions.client.UserCredentials;

import java.io.Serializable;

/**
 * Created by juraj on 3/18/17.
 */
public class SessionCreateRequest implements Serializable {

    private final String clientId;
    private final String clientActorAddress;
    private final HostData hostData;
    private final UserCredentials userCredentials;

    public SessionCreateRequest(String clientId, String clientActorAddress,
                                HostData hostData, UserCredentials userCredentials) {
        this.clientId = clientId;
        this.clientActorAddress = clientActorAddress;
        this.hostData = hostData;
        this.userCredentials = userCredentials;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientActorAddress() {
        return clientActorAddress;
    }

    public HostData getHostData() {
        return hostData;
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

}
