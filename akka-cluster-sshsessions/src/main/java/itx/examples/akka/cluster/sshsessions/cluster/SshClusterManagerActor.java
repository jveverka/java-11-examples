package itx.examples.akka.cluster.sshsessions.cluster;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import itx.examples.akka.cluster.sshsessions.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by juraj on 3/18/17.
 */
public class SshClusterManagerActor extends UntypedActor {

    private static final Logger LOG = LoggerFactory.getLogger(SshClusterManagerActor.class);

    private SshClusterManagerImpl sshClusterManager;
    private Cluster cluster;

    public SshClusterManagerActor(SshClusterManagerImpl sshClusterManager) {
        this.sshClusterManager = sshClusterManager;
        this.cluster = Cluster.get(getContext().system());
        sshClusterManager.setContext(getContext().system(), self(), cluster.selfAddress().toString());
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof ClusterEvent.LeaderChanged) {
            ClusterEvent.LeaderChanged leaderChanged = (ClusterEvent.LeaderChanged) message;
            sshClusterManager.setLeaderAddress(leaderChanged.getLeader().toString());
        } else if (message instanceof ClusterEvent.MemberUp) {
            ClusterEvent.MemberUp memberUp = (ClusterEvent.MemberUp)message;
            sshClusterManager.addMember(memberUp.member().address().toString(), "UP");
        } else if (message instanceof ClusterEvent.MemberExited) {
            ClusterEvent.MemberExited memberExited = (ClusterEvent.MemberExited)message;
            sshClusterManager.removeMember(memberExited.member().address().toString(), "EXITED");
        } else if (message instanceof ClusterEvent.MemberJoined) {
            ClusterEvent.MemberJoined memberJoined = (ClusterEvent.MemberJoined)message;
            sshClusterManager.addMember(memberJoined.member().address().toString(), "JOINED");
        } else if (message instanceof ClusterEvent.MemberLeft) {
            ClusterEvent.MemberLeft memberLeft = (ClusterEvent.MemberLeft)message;
            sshClusterManager.removeMember(memberLeft.member().address().toString(), "LEFT");
        } else if (message instanceof ClusterEvent.MemberRemoved) {
            ClusterEvent.MemberRemoved memberRemoved = (ClusterEvent.MemberRemoved)message;
            sshClusterManager.removeMember(memberRemoved.member().address().toString(), "REMOVED");
        } else if (message instanceof ClusterEvent.MemberWeaklyUp) {
            ClusterEvent.MemberWeaklyUp memberWeaklyUp = (ClusterEvent.MemberWeaklyUp)message;
            sshClusterManager.removeMember(memberWeaklyUp.member().address().toString(), "WEAKLY_UP");

        } else if (message instanceof SessionCreateRequest) {
            SessionCreateRequest sessionCreateRequest = (SessionCreateRequest)message;
            sshClusterManager.onSessionCreateRequest(sessionCreateRequest);
        } else if (message instanceof SessionCreateResponse) {
            SessionCreateResponse sessionCreateResponse = (SessionCreateResponse)message;
            sshClusterManager.onSessionCreateResponse(sessionCreateResponse);
        } else if (message instanceof SessionCloseRequest) {
            SessionCloseRequest sessionCloseRequest = (SessionCloseRequest) message;
            sshClusterManager.onSessionCloseRequest(sessionCloseRequest);
        } else if (message instanceof SessionCloseResponse) {
            SessionCloseResponse sessionCloseResponse = (SessionCloseResponse) message;
            sshClusterManager.onSessionCloseResponse(sessionCloseResponse);
        } else if (message instanceof GetActiveSessionsResponse) {
            GetActiveSessionsResponse getActiveSessionsResponse = (GetActiveSessionsResponse)message;
            sshClusterManager.onActiveSessionsResponse(getActiveSessionsResponse);
        } else if (message instanceof SessionCreateError) {
            SessionCreateError sessionCreateError = (SessionCreateError)message;
            sshClusterManager.onSessionCreateError(sessionCreateError);
        } else {
            LOG.info("onReceive: {}", message.getClass().getName());
        }
    }

    @Override
    public void preStart() {
        LOG.info("preStart");
        cluster.subscribe(getSelf(),
                ClusterEvent.initialStateAsEvents(),
                ClusterEvent.LeaderChanged.class,
                ClusterEvent.MemberUp.class,
                ClusterEvent.MemberExited.class,
                ClusterEvent.MemberJoined.class,
                ClusterEvent.MemberLeft.class,
                ClusterEvent.MemberRemoved.class,
                ClusterEvent.MemberWeaklyUp.class);
    }

    @Override
    public void postStop() {
        LOG.info("postStop");
        cluster.unsubscribe(getSelf());
    }

}
