package itx.hazelcast.cluster.server.hazelcast;

import com.hazelcast.core.MemberAttributeEvent;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MembershipListenerImpl implements MembershipListener {

    private final static Logger LOG = LoggerFactory.getLogger(MembershipListenerImpl.class);

    private CountDownLatch cl;

    public MembershipListenerImpl(int expectedClusterSize) {
        if (expectedClusterSize > 1) {
            this.cl = new CountDownLatch(expectedClusterSize - 1);
        }
    }

    @Override
    public void memberAdded(MembershipEvent membershipEvent) {
        LOG.info("memberAdded: {} {}", membershipEvent.getMember().getUuid(), membershipEvent.getMember().getAddress());
        cl.countDown();
    }

    @Override
    public void memberRemoved(MembershipEvent membershipEvent) {
        LOG.info("memberRemoved: {} {}", membershipEvent.getMember().getUuid(), membershipEvent.getMember().getAddress());
    }

    @Override
    public void memberAttributeChanged(MemberAttributeEvent memberAttributeEvent) {
        LOG.info("memberAttributeChanged: {} {}={}",
                memberAttributeEvent.getMember().getUuid(),
                memberAttributeEvent.getKey(), memberAttributeEvent.getValue().toString());
    }

    public void awaitClusterFormation(long duration, TimeUnit timeUnit) {
        if (cl != null) {
            try {
                cl.await(duration, timeUnit);
                LOG.info("CLUSTER OK");
            } catch (InterruptedException e) {
                LOG.warn("Cluster not formed in expected time frame {} {}", duration, timeUnit.name());
                LOG.warn("Continuing anyway ...");
            }
        } else {
            LOG.info("NO CLUSTER MEMBERS EXPECTED.");
        }
    }
}
