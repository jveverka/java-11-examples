package itx.hazelcast.cluster.server.services;

public interface MessageService {

    void publishMessage(String topic, String message);

    MessageServiceSubscription subscribe(String topic, MessageServiceListener listener);

    void unSubscribe(MessageServiceSubscription messageServiceSubscription);

}
