package itx.examples.akka.cluster.sshsessions.client.impl;

import akka.actor.ActorRef;
import itx.examples.akka.cluster.sshsessions.client.SshClientServiceImpl;
import itx.examples.akka.cluster.sshsessions.client.SshClientSession;
import itx.examples.akka.cluster.sshsessions.client.SshClientSessionListener;
import itx.examples.akka.cluster.sshsessions.dto.SessionDataRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by juraj on 3/19/17.
 */
public class SshClientSessionImpl implements SshClientSession {

    private static final Logger LOG = LoggerFactory.getLogger(SshClientSessionImpl.class);

    private String id;
    private SshClientSessionListener sshClientSessionListener;
    private ActorRef sshClientActor;
    private ActorRef sshSessionActor;
    private SshClientServiceImpl sshClientService;
    private String sessionActorAddress;
    private String sshSessionId;

    public SshClientSessionImpl(String id, SshClientSessionListener sshClientSessionListener,
                                ActorRef sshClientActor, SshClientServiceImpl sshClientService) {
        this.id = id;
        this.sshClientSessionListener = sshClientSessionListener;
        this.sshClientActor = sshClientActor;
        this.sshClientService = sshClientService;
    }

    public void setSshSessionActor(ActorRef sshSessionActor, String sessionActorAddress, String sshSessionId) {
        this.sshSessionActor = sshSessionActor;
        this.sessionActorAddress = sessionActorAddress;
        this.sshSessionId = sshSessionId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void sendData(String data) {
        LOG.info("sending data: {}", data);
        SessionDataRequest sessionDataRequest = new SessionDataRequest(data);
        sshSessionActor.tell(sessionDataRequest, sshClientActor);
    }

    @Override
    public void close() throws Exception {
        sshClientService.onCloseSessionRequest(id, sessionActorAddress, sshSessionId);
    }

    public void onDataReceived(String data) {
        if (sshClientSessionListener != null) {
            sshClientSessionListener.onData(data);
        }
    }

    public void onClose() {
        if (sshClientSessionListener != null) {
            sshClientSessionListener.onClose();
        }
    }

    public void onError() {
        if (sshClientSessionListener != null) {
            sshClientSessionListener.onError("closed by server");
        }
    }

}
