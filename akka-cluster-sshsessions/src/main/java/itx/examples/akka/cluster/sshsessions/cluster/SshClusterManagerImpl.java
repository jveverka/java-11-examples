package itx.examples.akka.cluster.sshsessions.cluster;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import itx.examples.akka.cluster.sshsessions.Utils;
import itx.examples.akka.cluster.sshsessions.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by juraj on 3/18/17.
 */
public class SshClusterManagerImpl implements SshClusterManager {

    private static final Logger LOG = LoggerFactory.getLogger(SshClusterManagerImpl.class);

    private String leaderNodeAddress;
    private String selfNodeAddress;
    private Map<String, MemberInfo> members;  //indexed by memberAddress
    private AtomicBoolean isLeader;
    private ActorSystem actorSystem;
    private ActorRef selfRef;
    private Map<String, SessionCreateInfo> pendingCreateSessionRequests; //indexed by clientId
    private Map<String, SessionCloseRequest> pendingCloseSessionRequests; //indexed by clientId

    public SshClusterManagerImpl() {
        this.members = new ConcurrentHashMap<>();
        this.pendingCreateSessionRequests = new ConcurrentHashMap<>();
        this.pendingCloseSessionRequests = new ConcurrentHashMap<>();
        this.isLeader = new AtomicBoolean(false);
    }

    public void setContext(ActorSystem actorSystem, ActorRef selfRef, String selfNodeAddress) {
        this.selfNodeAddress = selfNodeAddress;
        this.selfRef = selfRef;
        this.actorSystem = actorSystem;
    }

    public void setLeaderAddress(String leaderAddress) {
        this.leaderNodeAddress = leaderAddress;
        if (this.selfNodeAddress.equals(leaderAddress)) {
            isLeader.set(true);
            //TODO: get session info from local managers
            requestAllMemberSessions();
        } else {
            isLeader.set(false);
        }
        printMembers();
    }

    public void addMember(String memberAddress, String status) {
        LOG.info("addMember: {} {} {}", selfNodeAddress, memberAddress, status);
        if (members.get(memberAddress) == null ) {
            members.put(memberAddress, new MemberInfo(memberAddress));
            printMembers();
            if (isLeader.get()) {
                //TODO: get session info from local managers
                requestMemberSessions(memberAddress);
            }
        }
    }

    public void removeMember(String memberAddress, String status) {
        LOG.info("removeMember: {} {} {}", selfNodeAddress, memberAddress, status);
        MemberInfo removedMember = members.remove(memberAddress);
        printMembers();
        if (isLeader.get() && removedMember != null) {
            killOrphanedClientsAndSessions(removedMember);
        }
    }

    public void onSessionCreateRequest(SessionCreateRequest sessionCreateRequest) {
        if (isLeader.get()) {
            String memberAddress = selectClusterMemberForNewSession();
            ActorSelection actorSelection = actorSystem.actorSelection(Utils.getSshLocalManagerActorAddress(memberAddress));
            actorSelection.tell(sessionCreateRequest, selfRef);
            //TODO: set timeout (circuit breaker)
            SessionCreateInfo sessionCreateInfo =
                    new SessionCreateInfo(sessionCreateRequest.getClientId(), memberAddress, sessionCreateRequest.getClientActorAddress());
            pendingCreateSessionRequests.put(sessionCreateRequest.getClientId(), sessionCreateInfo);
        }
    }

    public void onSessionCreateResponse(SessionCreateResponse sessionCreateResponse) {
        if (isLeader.get()) {
            LOG.info("session created response: {}", sessionCreateResponse.getSessionId());
            SessionCreateInfo sessionCreateInfo = pendingCreateSessionRequests.remove(sessionCreateResponse.getClientId());
            if (sessionCreateInfo != null) {
                SessionInfo sessionInfo = new SessionInfo(null,
                        sessionCreateResponse.getClientId(),
                        sessionCreateResponse.getSshLocalSessionManagerActorAddress(),
                        sessionCreateResponse.getSessionId());
                members.get(sessionCreateInfo.getMemberAddress()).addSession(sessionInfo);
                actorSystem.actorSelection(sessionCreateInfo.getClientActorAddress()).tell(sessionCreateResponse, selfRef);
            }
        }
    }

    public void onSessionCloseRequest(SessionCloseRequest sessionCloseRequest) {
        if (isLeader.get()) {
            LOG.info("session close request: {}", sessionCloseRequest.getSessionActorAddress());
            pendingCloseSessionRequests.put(sessionCloseRequest.getClientId(), sessionCloseRequest);
            for (MemberInfo mi: members.values()) {
                String localManagerAddress = mi.getSshSessionLocalManagerAddress(sessionCloseRequest.getSshSessionId());
                if (localManagerAddress != null) {
                    ActorSelection actorSelection = actorSystem.actorSelection(localManagerAddress);
                    actorSelection.tell(sessionCloseRequest, selfRef);
                    break;
                }
            }
        }
    }

    public void onSessionCloseResponse(SessionCloseResponse sessionCloseResponse) {
        if (isLeader.get()) {
            LOG.info("session close response: {}", sessionCloseResponse.getSessionId());
            SessionCloseRequest sessionCloseRequest = pendingCloseSessionRequests.remove(sessionCloseResponse.getClientId());
            if (sessionCloseRequest != null) {
                for (MemberInfo memberInfo : members.values()) {
                    if (sessionCloseRequest.getSessionActorAddress().startsWith(memberInfo.getMemberAddress())) {
                        memberInfo.removeSessionBySessionId(sessionCloseResponse.getSessionId());
                        break;
                    }
                }
                ActorSelection actorSelection = actorSystem.actorSelection(sessionCloseResponse.getClientActorAddress());
                actorSelection.tell(sessionCloseResponse, selfRef);
            }
        }
    }

    public void onActiveSessionsResponse(GetActiveSessionsResponse activeSessionsResponse) {
        if (isLeader.get()) {
            LOG.info("onActiveSessionsResponse: [{}] {}", activeSessionsResponse.getActiveSessions().size() ,
                    activeSessionsResponse.getMemberAddress());
            MemberInfo memberInfo = members.get(activeSessionsResponse.getMemberAddress());
            if (memberInfo != null) {
                activeSessionsResponse.getActiveSessions().forEach( si -> {
                    String sshLocalSessionManagerActorAddress =
                            Utils.getSshLocalManagerActorAddress(activeSessionsResponse.getMemberAddress());
                    SessionInfo sessionInfo = new SessionInfo(si.getClientSessionActorAddress(), si.getClientId(),
                            sshLocalSessionManagerActorAddress, si.getSessionId());
                    memberInfo.addSession(sessionInfo);
                });
            }
        }
    }

    public void onSessionCreateError(SessionCreateError sessionCreateError) {
        if (isLeader.get()) {
            //TODO: handle ssh session create error

        }
    }

    public String getSshClusterManagerLeaderAddress() {
        return Utils.getSshClusterManagerAddress(leaderNodeAddress);
    }

    public String getSelfNodeAddress() {
        return selfNodeAddress;
    }

    private void requestAllMemberSessions() {
        members.values().forEach( m -> {
            requestMemberSessions(m.getMemberAddress());
        });
    }

    private void requestMemberSessions(String memberAddress) {
        actorSystem.actorSelection(Utils.getSshLocalManagerActorAddress(memberAddress))
                .tell(new GetActiveSessionsRequest(), selfRef);
    }

    private void killOrphanedClientsAndSessions(MemberInfo removedMember) {
        LOG.info("killOrphanedClientsAndSessions {}", removedMember.getMemberAddress());
        //TODO: close orphaned clients and sessions
    }

    private String selectClusterMemberForNewSession() {
        //select cluster node with minimum ssh sessions
        String memberAddress = null;
        int minSessions = Integer.MAX_VALUE;
        for (MemberInfo memberInfo: members.values()) {
            if (minSessions > memberInfo.getSessionCount()) {
                minSessions = memberInfo.getSessionCount();
                memberAddress = memberInfo.getMemberAddress();
            }
        }
        LOG.info("selected node [{}] {}", minSessions, memberAddress);
        return memberAddress;
    }

    private void printMembers() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        members.forEach( (k,v) -> {
           sb.append("  ");
           sb.append(k.equals(selfNodeAddress)?"->":"  ");
           sb.append(k.equals(leaderNodeAddress)?"LEADER":"follow");
           sb.append(" [");
           sb.append(v.getSessionCount());
           sb.append("] ");
           sb.append(k);
           sb.append("\n");
        } );
        LOG.info(sb.toString());
    }

}
