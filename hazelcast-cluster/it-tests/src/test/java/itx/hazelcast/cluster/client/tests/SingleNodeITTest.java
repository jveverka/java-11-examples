package itx.hazelcast.cluster.client.tests;

import itx.hazelcast.cluster.client.wsclient.WsClient;
import itx.hazelcast.cluster.dto.DataMessage;
import itx.hazelcast.cluster.dto.DataMessageEvent;
import itx.hazelcast.cluster.dto.MessageWrapper;
import itx.hazelcast.cluster.dto.SubscribeToTopicRequest;
import itx.hazelcast.cluster.dto.SubscribeToTopicResponse;
import itx.hazelcast.cluster.dto.UnsubscribeFromTopicRequest;
import itx.hazelcast.cluster.dto.UnsubscribeFromTopicResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SingleNodeITTest {

    @Test
    public void testSingleNodeClientMessage() throws Exception {
        String topic = "topic1";
        BlockingSessionListener sessionListener = new BlockingSessionListener();

        // start client
        WsClient client = new WsClient("ws://localhost:8080/data/websocket", sessionListener);
        client.start();
        sessionListener.awaitForSessionReady(10, TimeUnit.SECONDS);

        // subscribe to topic and wait for subscription
        sessionListener.resetMessageBuffer(1);
        SubscribeToTopicRequest subscribeToTopicRequest = SubscribeToTopicRequest.newBuilder()
                .setRequestId(1)
                .setTopicId(topic)
                .build();
        MessageWrapper messageWrapper = MessageWrapper.newBuilder()
                .setSubscribeToTopicRequest(subscribeToTopicRequest)
                .build();

        client.sendMessageWrapper(messageWrapper);

        List<MessageWrapper> messageWrappers = sessionListener.awaitMessages(10, TimeUnit.SECONDS);
        SubscribeToTopicResponse subscribeToTopicResponse = messageWrappers.get(0).getSubscribeToTopicResponse();
        Assert.assertNotNull(subscribeToTopicResponse);
        Assert.assertEquals(subscribeToTopicResponse.getRequestId(), subscribeToTopicRequest.getRequestId());
        Assert.assertNotNull(subscribeToTopicResponse.getSubscriptionId());

        // send data message to topic and wait for message event from same topic
        sessionListener.resetMessageBuffer(1);
        DataMessage dataMessage = DataMessage.newBuilder()
                .setTopicId(topic)
                .setMessage("hello world")
                .build();
        messageWrapper = MessageWrapper.newBuilder()
                .setDataMessage(dataMessage)
                .build();
        client.sendMessageWrapper(messageWrapper);

        messageWrappers = sessionListener.awaitMessages(10, TimeUnit.SECONDS);
        DataMessageEvent dataMessageEvent = messageWrappers.get(0).getDataMessageEvent();
        Assert.assertNotNull(dataMessageEvent);
        Assert.assertEquals(dataMessage.getMessage(), dataMessageEvent.getMessage());
        Assert.assertEquals(dataMessage.getTopicId(), dataMessageEvent.getTopicId());
        Assert.assertEquals(subscribeToTopicResponse.getSubscriptionId(), dataMessageEvent.getSubscriptionId());

        // unsubscribe from topic and wait for confirmation
        sessionListener.resetMessageBuffer(1);
        UnsubscribeFromTopicRequest unsubscribeFromTopicRequest = UnsubscribeFromTopicRequest.newBuilder()
                .setRequestId(2)
                .setSubscriptionId(subscribeToTopicResponse.getSubscriptionId())
                .build();
        messageWrapper = MessageWrapper.newBuilder()
                .setUnsubscribeFromTopicRequest(unsubscribeFromTopicRequest)
                .build();
        client.sendMessageWrapper(messageWrapper);
        messageWrappers = sessionListener.awaitMessages(10, TimeUnit.SECONDS);
        UnsubscribeFromTopicResponse unsubscribeFromTopicResponse = messageWrappers.get(0).getUnsubscribeFromTopicResponse();
        Assert.assertNotNull(unsubscribeFromTopicResponse);
        Assert.assertEquals(unsubscribeFromTopicRequest.getRequestId(), unsubscribeFromTopicResponse.getRequestId());

        // stop client
        client.stop();
    }

}
