package itx.examples.akka.cluster.sshsessions.sessions;

import akka.actor.ActorRef;
import itx.examples.akka.cluster.sshsessions.client.SshClientSession;

/**
 * Created by juraj on 25.3.2017.
 */
public class ActiveSessionData {

    private String sessionId;
    private ActorRef sessionActor;
    private String clientId;
    private String clientSessionActorAddress;
    private SshClientSession sshSession;

    public ActiveSessionData(String sessionId, ActorRef sessionActor,
                             String clientId, String clientSessionActorAddress,
                             SshClientSession sshSession) {
        this.sessionId = sessionId;
        this.sessionActor = sessionActor;
        this.clientId = clientId;
        this.clientSessionActorAddress = clientSessionActorAddress;
        this.sshSession = sshSession;
    }

    public String getSessionId() {
        return sessionId;
    }

    public ActorRef getSessionActor() {
        return sessionActor;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSessionActorAddress() {
        return clientSessionActorAddress;
    }

    public SshClientSession getSshSession() {
        return sshSession;
    }

}
