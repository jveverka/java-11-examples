package itx.examples.akka.cluster.sshsessions.dto;

import java.io.Serializable;

/**
 * Created by juraj on 3/18/17.
 */
public class SessionPongMessage implements Serializable {

    private final String sessionId;
    private final String memberAddress;

    public SessionPongMessage(String memberAddress, String sessionId) {
        this.sessionId = sessionId;
        this.memberAddress = memberAddress;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

}
