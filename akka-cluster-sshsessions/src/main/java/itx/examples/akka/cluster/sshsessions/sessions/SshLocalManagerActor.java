package itx.examples.akka.cluster.sshsessions.sessions;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import itx.examples.akka.cluster.sshsessions.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by juraj on 3/19/17.
 */
public class SshLocalManagerActor extends UntypedActor {

    private static final Logger LOG = LoggerFactory.getLogger(SshLocalManagerActor.class);

    private SshLocalManagerImpl localManager;

    public SshLocalManagerActor(SshLocalManagerImpl localManager) {
        this.localManager = localManager;
    }

    @Override
    public void preStart() {
        String nodeAddress = Cluster.get(context().system()).selfAddress().toString();
        localManager.setNodeAddress(nodeAddress);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof SessionCreateRequest) {
            SessionCreateRequest sessionCreateRequest = (SessionCreateRequest) message;
            localManager.onSessionCreateResquest(context(), sender(), self(), sessionCreateRequest);
        } else if (message instanceof SessionCloseRequest) {
            SessionCloseRequest sessionCloseRequest = (SessionCloseRequest)message;
            localManager.onSessionCloseRequest(self(), sender(), sessionCloseRequest);
        } else if (message instanceof SessionCloseResponse) {
            SessionCloseResponse sessionCloseResponse = (SessionCloseResponse)message;
            localManager.onSessionCloseResponse(self(), sessionCloseResponse);
        } else if (message instanceof GetActiveSessionsRequest) {
            GetActiveSessionsResponse activeSessionsResponse = localManager.onGetActiveSessionsRequest();
            sender().tell(activeSessionsResponse, self());
        } else {
            LOG.info("onReceive: {}", message.getClass().getName());
        }
    }

}
