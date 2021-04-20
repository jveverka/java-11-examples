package itx.hazelcast.cluster.server.services;

public class MessageServiceSubscriptionImpl implements MessageServiceSubscription {

    private final String topic;
    private final String id;

    public MessageServiceSubscriptionImpl(String topic, String id) {
        this.topic = topic;
        this.id = id;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getId() {
        return id;
    }
}
