package itx.hazelcast.cluster.server.services;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageServiceImpl implements MessageService {

    private final HazelcastInstance hazelcastInstance;
    private final Map<String, IList<String>> messageServiceCaches;

    public MessageServiceImpl(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        this.messageServiceCaches = new ConcurrentHashMap<>();
    }

    @Override
    public void publishMessage(String topic, String message) {
        IList<String> messages = messageServiceCaches.get(topic);
        if (messages == null) {
            messages = hazelcastInstance.getList(topic);
            messageServiceCaches.put(topic, messages);
        }
        messages.add(message);
    }

    @Override
    public MessageServiceSubscription subscribe(String topic, MessageServiceListener listener) {
        IList<String> messages = messageServiceCaches.get(topic);
        if (messages == null) {
            messages = hazelcastInstance.getList(topic);
            messageServiceCaches.put(topic, messages);
        }
        ItemListenerImpl itemListener = new ItemListenerImpl(listener);
        String subscriptionId = messages.addItemListener(itemListener, true);
        return new MessageServiceSubscriptionImpl(topic, subscriptionId);
    }

    @Override
    public void unSubscribe(MessageServiceSubscription messageServiceSubscription) {
        IList<String> messages = messageServiceCaches.get(messageServiceSubscription.getTopic());
        if (messages != null) {
            messages.removeItemListener(messageServiceSubscription.getId());
        }
    }

}
