/*
 * Copyright © 2017 Pantheon Technologies, s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package itx.examples.akka.cluster.sshsessions.tests.utils;

import akka.japi.Creator;

/**
 * Created by juraj on 19.1.2017.
 */
public class ClusterStatusObserverActorCreator implements Creator<ClusterStatusObserverActor> {

    private ClusterStatusObserver clusterStatusObserver;

    public ClusterStatusObserverActorCreator(ClusterStatusObserver clusterStatusObserver) {
        this.clusterStatusObserver = clusterStatusObserver;
    }

    @Override
    public ClusterStatusObserverActor create() throws Exception {
        return new ClusterStatusObserverActor(clusterStatusObserver);
    }

}
