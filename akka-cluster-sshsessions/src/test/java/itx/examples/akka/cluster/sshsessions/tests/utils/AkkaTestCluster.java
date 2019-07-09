/*
 * Copyright © 2017 Pantheon Technologies, s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package itx.examples.akka.cluster.sshsessions.tests.utils;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import itx.examples.akka.cluster.sshsessions.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by juraj on 3/17/17.
 */
public class AkkaTestCluster {

    private static final Logger LOG = LoggerFactory.getLogger(AkkaTestCluster.class);
    public static final String DEFAULT_HOST_NAME = "127.0.0.1";
    public static final int DEFAULT_PORT = 2552;

    private int clusterSize;
    private List<ClusterNode> nodes;
    private Map<Integer, ClusterObjectRegistry> clusterObjects = new ConcurrentHashMap<>();
    private boolean clusterStarted;
    private String seedNodeListString;

    public AkkaTestCluster(int clusterSize) {
        this.clusterSize = clusterSize;
        this.nodes = new ArrayList<>();
        for (int i=0; i<clusterSize; i++) {
            ClusterNode clusterNode = new ClusterNode(DEFAULT_HOST_NAME, DEFAULT_PORT + i);
            nodes.add(clusterNode);
        }
        String[] seedNodeList = new String[clusterSize];
        for (int i=0; i<clusterSize; i++) {
            seedNodeList[i] = generateNodeAddress(
                    Utils.CLUSTER_NAME,
                    nodes.get(i).getHostName(),
                    nodes.get(i).getPort());
        }
        seedNodeListString = String.join(",\n",seedNodeList);
    }

    public void startCluster(long timeoutWaitForLeader, TimeUnit timeUnit) throws IOException {
        LOG.info("starting akka cluster ...");
        long duration = System.currentTimeMillis();
        ClusterStatusObserver clusterStatusObserver = new ClusterStatusObserver(clusterSize);
        ClusterStatusObserverActorCreator clusterStatusObserverActorCreator
                = new ClusterStatusObserverActorCreator(clusterStatusObserver);
        if (clusterSize == 1) {
            ActorSystem actorSystem = ActorSystem.create(
                    Utils.CLUSTER_NAME, ConfigFactory.load("single-node1"));
            clusterObjects.put(0, new ClusterObjectRegistry(0, actorSystem, clusterStatusObserver));

        } else if (clusterSize > 1) {
            for (int i = 0; i < clusterSize; i++) {
                final ActorSystem actorSystem = ActorSystem.create(
                            Utils.CLUSTER_NAME, ConfigFactory.parseString(cloneConfig(i)));
                actorSystem.actorOf(Props.create(clusterStatusObserverActorCreator), "test-cluster-satus-observer");
                clusterObjects.put(i, new ClusterObjectRegistry(i, actorSystem, clusterStatusObserver));
            }
        }

        clusterStatusObserver.waitForAllMembersWithLeader(timeoutWaitForLeader, timeUnit);
        clusterStarted = true;
        duration = System.currentTimeMillis() - duration;
        LOG.info(MessageFormat.format("akka cluster started in {0} ms", duration));
    }

    public void stopCluster() throws Exception {
        LOG.info("cluster shutdown ...");
        long duration = System.currentTimeMillis();
        for (ClusterObjectRegistry clusterObjectRegistry: clusterObjects.values()) {
            clusterObjectRegistry.getActorSystem().terminate();
        }

        LOG.info("waiting for cluster to shutdown !");
        await().atMost(15, TimeUnit.SECONDS).until(() ->
                clusterObjects.values().stream().allMatch(c -> c.getActorSystem().terminate().isCompleted()));
        clusterStarted = false;
        duration = System.currentTimeMillis() - duration;
        LOG.info(MessageFormat.format("cluster stopped in {0} ms", duration));
    }

    public boolean isClusterStarted() {
        return clusterStarted;
    }

    public int getSize() {
        return clusterSize;
    }

    public ClusterObjectRegistry getClusterObjectRegistry(int ordinal) {
        return clusterObjects.get(ordinal);
    }

    private String cloneConfig(int clusterOrdinal) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStream resourceAsStream = AkkaTestCluster.class.getClassLoader().getResourceAsStream("nodex.conf.template");
        BufferedReader in = new BufferedReader(new InputStreamReader(resourceAsStream));
        String line = null;
        while((line = in.readLine()) != null) {
            if (line.contains("__host_name__")) {
                line = line.replace("__host_name__", nodes.get(clusterOrdinal).getHostName());
            }
            if (line.contains("__port__")) {
                line = line.replace("__port__", Integer.toString(nodes.get(clusterOrdinal).getPort()));
            }
            if (line.contains("__seed_node_list__")) {
                line = line.replace("__seed_node_list__", seedNodeListString);
            }
            sb.append(line);
            sb.append("\n");
        }
        String config = sb.toString();
        return config;
    }

    private String generateNodeAddress(String clusterName, String hostName, int port) {
        return "\"akka.tcp://" + clusterName + "@" + hostName + ":" + port + "\"";
    }

}
