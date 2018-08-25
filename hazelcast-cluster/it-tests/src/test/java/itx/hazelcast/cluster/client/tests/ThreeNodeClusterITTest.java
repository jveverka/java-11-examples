package itx.hazelcast.cluster.client.tests;

import itx.hazelcast.cluster.client.wsclient.WsClient;
import itx.hazelcast.cluster.dto.DataMessage;
import itx.hazelcast.cluster.dto.DataMessageEvent;
import itx.hazelcast.cluster.dto.MessageWrapper;
import itx.hazelcast.cluster.dto.SubscribeToTopicRequest;
import itx.hazelcast.cluster.dto.SubscribeToTopicResponse;
import itx.hazelcast.cluster.dto.UnsubscribeFromTopicRequest;
import itx.hazelcast.cluster.dto.UnsubscribeFromTopicResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ThreeNodeClusterITTest {

    final private static Logger LOG = LoggerFactory.getLogger(ThreeNodeClusterITTest.class);

    private static final int MAX_NODES = 3;
    private static final String TOPIC_NAME = "topic1";
    private BlockingSessionListener[] sessionListeners = new BlockingSessionListener[MAX_NODES];
    private WsClient[] clients = new WsClient[MAX_NODES];
    private SubscribeToTopicRequest[] subscribeRequests = new SubscribeToTopicRequest[MAX_NODES];
    private SubscribeToTopicResponse[] subscribeResponses = new SubscribeToTopicResponse[MAX_NODES];

    @BeforeClass
    public void init() {
        for (int i=0; i<MAX_NODES; i++) {
            sessionListeners[i] = new BlockingSessionListener();
            clients[i] = new WsClient("ws://localhost:808" + i + "/data/websocket", sessionListeners[i]);
        }
        for (int i=0; i<MAX_NODES; i++) {
            try {
                clients[i].start();
            } catch (Exception e) {
                Assert.fail();
            }
        }
    }

    @Test
    public void testTopicSubscription() {
        LOG.info("");
        for (int i=0; i<MAX_NODES; i++) {
            sessionListeners[i].resetMessageBuffer(1);

            SubscribeToTopicRequest subscribeToTopicRequest = SubscribeToTopicRequest.newBuilder()
                    .setRequestId(10 + i)
                    .setTopicId(TOPIC_NAME)
                    .build();
            MessageWrapper messageWrapper = MessageWrapper.newBuilder()
                    .setSubscribeToTopicRequest(subscribeToTopicRequest)
                    .build();
            subscribeRequests[i] = subscribeToTopicRequest;

            try {
                clients[i].sendMessageWrapper(messageWrapper);
            } catch (IOException e) {
                Assert.fail("Failed to subscribe", e);
            }
        }
        for (int i=0; i<MAX_NODES; i++) {
            try {
                List<MessageWrapper> messageWrappers = sessionListeners[i].awaitMessages(10, TimeUnit.SECONDS);
                SubscribeToTopicResponse subscribeToTopicResponse = messageWrappers.get(0).getSubscribeToTopicResponse();
                Assert.assertNotNull(subscribeToTopicResponse);
                Assert.assertEquals(subscribeToTopicResponse.getRequestId(), subscribeRequests[i].getRequestId());
                Assert.assertNotNull(subscribeToTopicResponse.getSubscriptionId());
                subscribeResponses[i] = subscribeToTopicResponse;
            } catch (InterruptedException e) {
                Assert.fail("Failed to receive subscribe confirmation", e);
            }
        }
    }

    @Test (dependsOnMethods={"testTopicSubscription"})
    public void testSendMessageToTopic() {
        for (int i=0; i<MAX_NODES; i++) {
            sessionListeners[i].resetMessageBuffer(1);
        }
        DataMessage dataMessage = DataMessage.newBuilder()
                .setTopicId(TOPIC_NAME)
                .setMessage("hello world")
                .build();
        MessageWrapper messageWrapper = MessageWrapper.newBuilder()
                .setDataMessage(dataMessage)
                .build();
        try {
            clients[0].sendMessageWrapper(messageWrapper);
        } catch (IOException e) {
            Assert.fail("Failed to subscribe", e);
        }
        for (int i=0; i<MAX_NODES; i++) {
            try {
                List<MessageWrapper> messageWrappers = sessionListeners[i].awaitMessages(10, TimeUnit.SECONDS);
                DataMessageEvent dataMessageEvent = messageWrappers.get(0).getDataMessageEvent();
                Assert.assertNotNull(dataMessageEvent);
                Assert.assertEquals(dataMessage.getMessage(), dataMessageEvent.getMessage());
                Assert.assertEquals(dataMessage.getTopicId(), dataMessageEvent.getTopicId());
                Assert.assertEquals(subscribeResponses[i].getSubscriptionId(), dataMessageEvent.getSubscriptionId());
            } catch (InterruptedException e) {
                Assert.fail("Failed to dataMessageEvent", e);
            }
        }
    }

    @Test (dependsOnMethods={"testSendMessageToTopic"})
    public void testTopicUnsubscribe() {
        UnsubscribeFromTopicRequest[] unsubscribeRequests = new UnsubscribeFromTopicRequest[MAX_NODES];
        for (int i=0; i<MAX_NODES; i++) {
            sessionListeners[i].resetMessageBuffer(1);
            UnsubscribeFromTopicRequest unsubscribeFromTopicRequest = UnsubscribeFromTopicRequest.newBuilder()
                    .setRequestId(20 + i)
                    .setSubscriptionId(subscribeResponses[i].getSubscriptionId())
                    .build();
            MessageWrapper messageWrapper = MessageWrapper.newBuilder()
                    .setUnsubscribeFromTopicRequest(unsubscribeFromTopicRequest)
                    .build();
            unsubscribeRequests[i] = unsubscribeFromTopicRequest;
            try {
                clients[i].sendMessageWrapper(messageWrapper);
            } catch (IOException e) {
                Assert.fail("Failed to unsubscribe", e);
            }
        }
        for (int i=0; i<MAX_NODES; i++) {
            try {
                List<MessageWrapper> messageWrappers = sessionListeners[i].awaitMessages(10, TimeUnit.SECONDS);
                UnsubscribeFromTopicResponse unsubscribeFromTopicResponse = messageWrappers.get(0).getUnsubscribeFromTopicResponse();
                Assert.assertNotNull(unsubscribeFromTopicResponse);
                Assert.assertEquals(unsubscribeRequests[i].getRequestId(), unsubscribeFromTopicResponse.getRequestId());
            } catch (InterruptedException e) {
                Assert.fail("Failed to receive unsubscribe confirmation", e);
            }
        }

    }

    @AfterClass
    public void shutdown() {
        for (int i=0; i<MAX_NODES; i++) {
            try {
                clients[i].stop();
            } catch (Exception e) {
                LOG.error("Error: ", e);
            }
        }
    }

}
