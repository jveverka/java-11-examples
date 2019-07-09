package itx.examples.akka.cluster.sshsessions.sessions;

import akka.actor.PoisonPill;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import itx.examples.akka.cluster.sshsessions.client.SshClientSession;
import itx.examples.akka.cluster.sshsessions.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by juraj on 3/18/17.
 */
public class SshSessionActor extends UntypedActor {

    private static final Logger LOG = LoggerFactory.getLogger(SshSessionActor.class);

    private SshClientSession sshSession;
    private String clientId;
    private String clientActorAddress;
    private SshClientSessionListenerImpl sshClientSessionListener;

    public SshSessionActor(SshClientSession sshSession, String clientId,
                           String clientActorAddress, SshClientSessionListenerImpl sshClientSessionListener) {
        this.sshSession = sshSession;
        this.clientId = clientId;
        this.clientActorAddress = clientActorAddress;
        this.sshClientSessionListener = sshClientSessionListener;
    }

    @Override
    public void preStart() {
        LOG.info("preStart");
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof SessionDataRequest) {
            SessionDataRequest sessionDataRequest = (SessionDataRequest) message;
            sshSession.sendData(sessionDataRequest.getData());
            String dataOut = sshClientSessionListener.awaitData();
            SessionDataResponse sessionDataResponse = new SessionDataResponse(dataOut);
            sender().tell(sessionDataResponse, self());
        } else if (message instanceof SessionPingMessage) {
            String memberAddress = Cluster.get(context().system()).selfAddress().toString();
            context().sender().tell(new SessionPongMessage(memberAddress, sshSession.getId()), self());
        } else if (message instanceof SessionCloseRequest) {
            SessionCloseRequest sessionCloseRequest = (SessionCloseRequest)message;
            sshSession.close();
            SessionCloseResponse sessionCloseResponse = new SessionCloseResponse(sshSession.getId(),
                    sessionCloseRequest.getClientId(), clientActorAddress);
            sender().tell(sessionCloseResponse, null);
            self().tell(PoisonPill.getInstance(), self());
        } else {
            LOG.info("onReceive: {}", message.getClass().getName());
            context().sender().tell(new SessionError(clientId,"unsupported message"), self());
        }
    }

    @Override
    public void postStop() {
        LOG.info("ssh session actor: bye");
    }

}
