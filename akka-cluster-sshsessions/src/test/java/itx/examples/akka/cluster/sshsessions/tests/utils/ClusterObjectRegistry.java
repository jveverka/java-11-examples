/*
 * Copyright © 2017 Pantheon Technologies, s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package itx.examples.akka.cluster.sshsessions.tests.utils;

import akka.actor.ActorSystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by juraj on 3/17/17.
 */
public class ClusterObjectRegistry {

    private int ordinal;
    private ActorSystem actorSystem;
    private ClusterStatusObserver clusterStatusObserver;
    private Map<Class, Object> objects;

    public ClusterObjectRegistry(int ordinal, ActorSystem actorSystem, ClusterStatusObserver clusterStatusObserver) {
        this.ordinal = ordinal;
        this.actorSystem = actorSystem;
        this.clusterStatusObserver = clusterStatusObserver;
        this.objects = new HashMap<>();
    }

    public int getOrdinal() {
        return ordinal;
    }

    public ActorSystem getActorSystem() {
        return actorSystem;
    }

    public ClusterStatusObserver getClusterStatusObserver() {
        return clusterStatusObserver;
    }

    public <T> void registerSingleObject(Class<T> clazz, T object) {
        objects.put(clazz, object);
    }

    public <T> T getSingleObject(Class<T> clazz) {
        return (T)objects.get(clazz);
    }

}
