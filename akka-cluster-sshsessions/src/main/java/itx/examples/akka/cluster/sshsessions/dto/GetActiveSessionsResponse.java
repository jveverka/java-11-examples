package itx.examples.akka.cluster.sshsessions.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by juraj on 25.3.2017.
 */
public class GetActiveSessionsResponse implements Serializable {

    private final String memberAddress;
    private final List<ActiveSession> activeSessions;

    public GetActiveSessionsResponse(String memberAddress, List<ActiveSession> activeSessions) {
        this.memberAddress = memberAddress;
        this.activeSessions = activeSessions;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public List<ActiveSession> getActiveSessions() {
        return activeSessions;
    }

}
