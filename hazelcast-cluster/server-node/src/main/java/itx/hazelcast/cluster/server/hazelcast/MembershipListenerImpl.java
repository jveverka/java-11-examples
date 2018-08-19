package itx.hazelcast.cluster.server.hazelcast;

import com.hazelcast.core.MemberAttributeEvent;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MembershipListenerImpl implements MembershipListener {

    final private static Logger LOG = LoggerFactory.getLogger(MembershipListenerImpl.class);

    public MembershipListenerImpl() {
    }

    @Override
    public void memberAdded(MembershipEvent membershipEvent) {
        LOG.info("memberAdded: {} {}", membershipEvent.getMember().getUuid(), membershipEvent.getMember().getAddress());
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
}
