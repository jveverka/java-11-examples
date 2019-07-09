package itx.examples.akka.cluster.sshsessions.client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import itx.examples.akka.cluster.sshsessions.Utils;
import itx.examples.akka.cluster.sshsessions.client.impl.SessionCreateInfo;
import itx.examples.akka.cluster.sshsessions.client.impl.SshClientActor;
import itx.examples.akka.cluster.sshsessions.client.impl.SshClientSessionImpl;
import itx.examples.akka.cluster.sshsessions.cluster.SshClusterManagerImpl;
import itx.examples.akka.cluster.sshsessions.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by juraj on 3/18/17.
 */
public class SshClientServiceImpl implements SshClientService {

    private static final Logger LOG = LoggerFactory.getLogger(SshClientServiceImpl.class);

    private ActorSystem actorSystem;
    private SshClusterManagerImpl sshClusterManager;
    private Map<String, SessionCreateInfo> pendingSessionCreateRequests; //indexed by clientId
    private Map<String, SshClientSessionImpl> activeClientSessions;  //indexed by clientId

    public SshClientServiceImpl(ActorSystem actorSystem, SshClusterManagerImpl sshClusterManager) {
        this.actorSystem = actorSystem;
        this.sshClusterManager = sshClusterManager;
        this.pendingSessionCreateRequests = new ConcurrentHashMap<>();
        this.activeClientSessions = new ConcurrentHashMap<>();
    }

    @Override
    public ListenableFuture<SshClientSession> createSession(HostData hostData,
                                                            UserCredentials userCredentials,
                                                            SshClientSessionListener sshClientSessionListener,
                                                            long timeout, TimeUnit timeUnit) {
        //1. create session create objects
        String clientId = UUID.randomUUID().toString();
        LOG.info("creating session for client {}", clientId);
        SettableFuture<SshClientSession> upcommingSession = SettableFuture.create();

        //2. register client actor
        ActorRef sshClientActor = actorSystem.actorOf(
                Props.create(SshClientActor.class, this, clientId, timeout, timeUnit),
                Utils.generateClientActorName(clientId));
        SshClientSessionImpl sshClientSession =
                new SshClientSessionImpl(clientId, sshClientSessionListener, sshClientActor, this);
        SessionCreateInfo sessionCreateInfo = new SessionCreateInfo(upcommingSession, sshClientSession);
        pendingSessionCreateRequests.put(clientId, sessionCreateInfo);

        //3. ask leader to create session on any cluster node
        String cmLeaderAddress = sshClusterManager.getSshClusterManagerLeaderAddress();
        LOG.info("ssh leader address: {}", cmLeaderAddress);
        String clientActorAddress = Utils.getSshClientAddress(sshClusterManager.getSelfNodeAddress(), clientId);
        SessionCreateRequest sessionCreateRequest = new SessionCreateRequest(clientId, clientActorAddress,
                hostData, userCredentials);
        actorSystem.actorSelection(cmLeaderAddress).tell(sessionCreateRequest, sshClientActor);

        return upcommingSession;
    }

    public void onSessionCreateReply(String clientId, String sshSessionId, ActorRef sshSessionActor, String sessionActorAddress) {
        LOG.info("onSessionCreateReply: {}", clientId);
        SessionCreateInfo sessionCreateInfo = pendingSessionCreateRequests.remove(clientId);
        if (sessionCreateInfo != null) {
            SettableFuture<SshClientSession> upcommingSession = sessionCreateInfo.getSessionSettableFuture();
            sessionCreateInfo.getSshClientSession().setSshSessionActor(sshSessionActor, sessionActorAddress, sshSessionId);
            activeClientSessions.put(clientId, sessionCreateInfo.getSshClientSession());
            upcommingSession.set(sessionCreateInfo.getSshClientSession());
        }
    }

    public void onSessionCreateTimeout(String clientId) {
        LOG.info("onSessionCreateTimeout: {}", clientId);
        SessionCreateInfo sessionCreateInfo = pendingSessionCreateRequests.remove(clientId);
        if (sessionCreateInfo != null) {
            SettableFuture<SshClientSession> upcommingSession = sessionCreateInfo.getSessionSettableFuture();
            upcommingSession.setException(new SshSessionTimeoutException("ssh session create timeout"));
        }
    }

    public void onSessionDataReceived(String clientId, SessionDataResponse sessionDataResponse) {
        SshClientSessionImpl sshClientSession = activeClientSessions.get(clientId);
        if (sshClientSession != null) {
            sshClientSession.onDataReceived(sessionDataResponse.getData());
        }
    }

    public void onCloseSessionRequest(String clientId, String sessionActorAddress, String sshSessionId) {
        LOG.info("onCloseSessionRequest: {}", clientId);
        SshClientSessionImpl sessionToRemove = activeClientSessions.remove(clientId);
        sessionToRemove.onClose();
        String cmLeaderAddress = sshClusterManager.getSshClusterManagerLeaderAddress();
        SessionCloseRequest sessionCloseRequest = new SessionCloseRequest(clientId, sessionActorAddress, sshSessionId);
        actorSystem.actorSelection(cmLeaderAddress).tell(sessionCloseRequest, null);
    }

    public void onCloseSessionResponse(String clientId, SessionCloseResponse sessionCloseResponse) {
        LOG.info("onCloseSessionResponse: {}", clientId);
        SshClientSessionImpl sessionToRemove = activeClientSessions.remove(clientId);
        if (sessionToRemove != null) {
            sessionToRemove.onError();
        }
    }

}