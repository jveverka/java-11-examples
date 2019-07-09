package itx.examples.akka.cluster.sshsessions.client.impl;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;
import akka.util.Timeout;
import itx.examples.akka.cluster.sshsessions.client.SshClientServiceImpl;
import itx.examples.akka.cluster.sshsessions.dto.SessionCloseResponse;
import itx.examples.akka.cluster.sshsessions.dto.SessionCreateResponse;
import itx.examples.akka.cluster.sshsessions.dto.SessionDataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by juraj on 3/18/17.
 */
public class SshClientActor extends UntypedActor {

    private static final Logger LOG = LoggerFactory.getLogger(SshClientActor.class);

    private SshClientServiceImpl sshClientService;
    private String clientId;
    private long timeout;
    private TimeUnit timeUnit;
    private ActorRef sshSessionActor;

    public SshClientActor(SshClientServiceImpl sshClientService, String clientId, long timeout, TimeUnit timeUnit) {
        this.sshClientService = sshClientService;
        this.clientId = clientId;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    @Override
    public void preStart() {
        //set initial timeout to detect session create failures (circuit brake pattern)
        Duration duration = Duration.create(timeout, timeUnit);
        context().setReceiveTimeout(duration);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof ReceiveTimeout) {
            sshClientService.onSessionCreateTimeout(clientId);
            self().tell(PoisonPill.getInstance(), self());
        } else if (message instanceof SessionCreateResponse) {
            SessionCreateResponse sessionCreateResponse = (SessionCreateResponse)message;
            LOG.info("looking for session actor: {}", sessionCreateResponse.getSessionActorAddress());
            Future<ActorRef> actorRefFuture = context().system()
                    .actorSelection(sessionCreateResponse.getSessionActorAddress()).resolveOne(Timeout.apply(2, TimeUnit.SECONDS));
            sshSessionActor = Await.result(actorRefFuture, Duration.apply(2, TimeUnit.SECONDS));
            sshClientService.onSessionCreateReply(sessionCreateResponse.getClientId(), sessionCreateResponse.getSessionId(), sshSessionActor, sessionCreateResponse.getSessionActorAddress());
        } else if (message instanceof SessionDataResponse) {
            SessionDataResponse sessionDataResponse = (SessionDataResponse)message;
            sshClientService.onSessionDataReceived(clientId, sessionDataResponse);
        } else if (message instanceof SessionCloseResponse) {
            SessionCloseResponse sessionCloseResponse = (SessionCloseResponse)message;
            sshClientService.onCloseSessionResponse(clientId, sessionCloseResponse);
            self().tell(PoisonPill.getInstance(), self());
        } else {
            LOG.info("onReceive: {}", message.getClass().getName());
        }
    }

    @Override
    public void postStop() {
        LOG.info("ssh client actor: bye");
    }

}
