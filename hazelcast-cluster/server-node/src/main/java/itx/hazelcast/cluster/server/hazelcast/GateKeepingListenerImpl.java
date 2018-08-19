package itx.hazelcast.cluster.server.hazelcast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GateKeepingListenerImpl implements GateKeepingListener {

    final private static Logger LOG = LoggerFactory.getLogger(GateKeepingListenerImpl.class);

    @Override
    public void onGateKeepingEvent() {
        LOG.info("onGateKeepingEvent: LEADER");
    }

}
