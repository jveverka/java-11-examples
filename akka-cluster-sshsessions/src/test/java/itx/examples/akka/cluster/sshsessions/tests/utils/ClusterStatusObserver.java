/*
 * Copyright © 2017 Pantheon Technologies, s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package itx.examples.akka.cluster.sshsessions.tests.utils;

import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by juraj on 19.1.2017.
 */
public class ClusterStatusObserver {

    private static final Logger LOG = LoggerFactory.getLogger(ClusterStatusObserver.class);

    private int maxMembers;
    private Map<String, Member> membersList;
    private AtomicBoolean hasLeader;

    public ClusterStatusObserver(int maxMembers) {
        this.maxMembers = maxMembers;
        this.membersList = new ConcurrentHashMap<>();
        this.hasLeader = new AtomicBoolean(false);
    }

    public void updateMember(Member member) {
        membersList.put(member.address().toString(), member);
        LOG.info("CSO: [" + membersList.size() + "]");
    }

    public void onLeaderChanged(ClusterEvent.LeaderChanged leaderChangedEvent) {
        hasLeader.set(leaderChangedEvent.leader().nonEmpty());
    }

    public void waitForAllMembers(long maxTime, TimeUnit timeUnit) {
        await().atMost(maxTime, timeUnit).until(() -> membersList.size() == maxMembers);
    }

    public void waitForLeader(long maxTime, TimeUnit timeUnit) {
        await().atMost(maxTime, timeUnit).untilTrue(hasLeader);
    }

    public void waitForAllMembersWithLeader(long maxTime, TimeUnit timeUnit) {
        await().atMost(maxTime, timeUnit).until(() -> (hasLeader.get() && (membersList.size() == maxMembers)));
        LOG.info("Cluster started with leader, memebers {}", membersList);
    }

}
