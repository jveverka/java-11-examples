package itx.examples.akka.cluster.sshsessions.cluster;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by juraj on 3/18/17.
 */
public class MemberInfo {

    private final String memberAddress;
    private final Map<String, SessionInfo> sessions; //indexed by sessionId

    public MemberInfo(String memberAddress) {
        this.memberAddress = memberAddress;
        this.sessions = new ConcurrentHashMap<>();
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public int getSessionCount() {
        return sessions.size();
    }

    public void addSession(SessionInfo sessionInfo) {
        sessions.put(sessionInfo.getSshSessionId(), sessionInfo);
    }

    public void removeSessionBySessionId(String sessionId) {
        sessions.remove(sessionId);
    }

    public void removeSessionByClientId(String clientId) {
        sessions.entrySet().removeIf(entry -> entry.getValue().getClientId().equals(clientId));
    }

    public String getSshSessionLocalManagerAddress(String sshSessionId) {
        SessionInfo sessionInfo = sessions.get(sshSessionId);
        if (sessionInfo != null) {
            return sessionInfo.getSshLocalSessionManagerActorAddress();
        }
        return null;
    }

}
