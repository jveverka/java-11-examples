/*
 * Copyright © 2017 Pantheon Technologies, s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package itx.examples.akka.cluster.sshsessions.tests.utils;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by juraj on 19.1.2017.
 */
public class ClusterStatusObserverActor extends UntypedActor {

    private static final Logger LOG = LoggerFactory.getLogger(ClusterStatusObserverActor.class);

    private ClusterStatusObserver clusterStatusObserver;
    private Cluster cluster;

    public ClusterStatusObserverActor(ClusterStatusObserver clusterStatusObserver) {
        this.clusterStatusObserver = clusterStatusObserver;
        this.cluster = Cluster.get(getContext().system());
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof ClusterEvent.MemberEvent) {
            Member member = ((ClusterEvent.MemberEvent) message).member();
            LOG.info("memberEvent: " + member.address().toString());
            clusterStatusObserver.updateMember(member);
        } else if (message instanceof ClusterEvent.LeaderChanged) {
            ClusterEvent.LeaderChanged leaderChangedEvent = (ClusterEvent.LeaderChanged)message;
            clusterStatusObserver.onLeaderChanged(leaderChangedEvent);
            LOG.info("leaderChanged: " + leaderChangedEvent.getLeader().toString());
        } else {
            LOG.info("onReceive: " + message.getClass().getName());
        }
    }

    @Override
    public void preStart() throws Exception {
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberUp.class, ClusterEvent.LeaderChanged.class);
    }

    @Override
    public void postStop() throws Exception {
        cluster.unsubscribe(getSelf());
    }

}
