package itx.hazelcast.cluster.server.services;

import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemListenerImpl implements ItemListener<String> {

    private final static Logger LOG = LoggerFactory.getLogger(ItemListenerImpl.class);

    private final MessageServiceListener listener;

    public ItemListenerImpl(MessageServiceListener listener) {
        this.listener = listener;
    }

    @Override
    public void itemAdded(ItemEvent<String> item) {
        LOG.info("itemAdded: {} {}", item.getEventType().getType(), item.getItem());
        listener.onMessage(item.getItem());
    }

    @Override
    public void itemRemoved(ItemEvent<String> item) {
        LOG.info("itemRemoved: {} {}", item.getEventType().getType(), item.getItem());
    }

}
