package itx.examples.akka.cluster.sshsessions.sessions;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import itx.examples.akka.cluster.sshsessions.Utils;
import itx.examples.akka.cluster.sshsessions.client.SshClientSession;
import itx.examples.akka.cluster.sshsessions.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by juraj on 25.3.2017.
 */
public class SshLocalManagerImpl implements SshLocalManager {

    private static final Logger LOG = LoggerFactory.getLogger(SshLocalManagerImpl.class);

    private SshSessionFactory sshSessionFactory;
    private String nodeAddress;
    private Map<String, ActiveSessionData> activeSessions;
    private Map<String, ActorRef> pendingSessionCloseRequests;

    public SshLocalManagerImpl(SshSessionFactory sshSessionFactory) {
        this.activeSessions = new ConcurrentHashMap<>();
        this.pendingSessionCloseRequests = new ConcurrentHashMap<>();
        this.sshSessionFactory = sshSessionFactory;
    }

    public void setNodeAddress(String nodeAddress) {
        this.nodeAddress = nodeAddress;
    }

    public void onSessionCreateResquest(ActorContext actorContext, ActorRef sender, ActorRef self, SessionCreateRequest sessionCreateRequest) {
        LOG.info("create session from: {}", sessionCreateRequest.getClientActorAddress());

        try {
            String sessionId = UUID.randomUUID().toString();

            //1. create session objects and actor
            SshClientSessionListenerImpl sshClientSessionListener = new SshClientSessionListenerImpl();
            SshClientSession sshSession = sshSessionFactory.createSshSession(sessionCreateRequest.getHostData(),
                    sessionCreateRequest.getUserCredentials(),
                    sessionId, sshClientSessionListener);
            ActorRef sshSessionActorRef = actorContext.system().actorOf(Props.create(
                    SshSessionActor.class, sshSession, sessionCreateRequest.getClientId(),
                    sessionCreateRequest.getClientActorAddress(), sshClientSessionListener),
                    Utils.generateSessionActorName(sessionId));

            //2. notify creator about ssh session creation result
            String sessionActorAddress = Utils.getSshSessionAddress(nodeAddress, sshSession.getId());
            SessionCreateResponse sessionCreateResponse =
                    new SessionCreateResponse(sshSession.getId(),
                            sessionCreateRequest.getClientId(), sessionActorAddress,
                            Utils.getSshLocalManagerActorAddress(nodeAddress));
            sender.tell(sessionCreateResponse, self);

            ActiveSessionData activeSessionInfo = new ActiveSessionData(
                    sessionCreateRequest.getClientId(), sshSessionActorRef,
                    sshSession.getId(), sessionCreateRequest.getClientActorAddress(), sshSession);
            activeSessions.put(sshSession.getId(), activeSessionInfo);
        } catch (SshClientException e) {
            SessionCreateError sessionError = new SessionCreateError(sessionCreateRequest.getClientId());
            sender.tell(sessionError, self);
        }
    }

    public void onSessionCloseRequest(ActorRef self, ActorRef sender, SessionCloseRequest sessionCloseRequest) {
        ActiveSessionData activeSessionInfo = activeSessions.get(sessionCloseRequest.getSshSessionId());
        if (activeSessionInfo != null) {
            activeSessionInfo.getSessionActor().tell(sessionCloseRequest, self);
            pendingSessionCloseRequests.put(sessionCloseRequest.getSshSessionId(), sender);
        }
    }

    public void onSessionCloseResponse(ActorRef self, SessionCloseResponse sessionCloseResponse) {
        activeSessions.remove(sessionCloseResponse.getSessionId());
        ActorRef sessionRemoveRequestor = pendingSessionCloseRequests.remove(sessionCloseResponse.getSessionId());
        if (sessionRemoveRequestor != null) {
            sessionRemoveRequestor.tell(sessionCloseResponse, self);
        }
    }

    public GetActiveSessionsResponse onGetActiveSessionsRequest() {
        return new GetActiveSessionsResponse(nodeAddress, getActiveSessions());
    }

    @Override
    public List<ActiveSession> getActiveSessions() {
        List<ActiveSession> activeSessionList = new ArrayList<>();
        activeSessions.values().forEach( as -> {
            ActiveSession activeSession = new ActiveSession(as.getSessionId(),
                    as.getClientId(), as.getClientSessionActorAddress());
            activeSessionList.add(activeSession);
        });
        return activeSessionList;
    }

}
