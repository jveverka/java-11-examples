package itx.hazelcast.cluster.server.services;

import itx.hazelcast.cluster.dto.DataMessage;
import itx.hazelcast.cluster.dto.DataMessageEvent;
import itx.hazelcast.cluster.dto.MessageWrapper;
import itx.hazelcast.cluster.dto.SubscribeToTopicRequest;
import itx.hazelcast.cluster.dto.SubscribeToTopicResponse;
import itx.hazelcast.cluster.dto.UnsubscribeFromTopicRequest;
import itx.hazelcast.cluster.dto.UnsubscribeFromTopicResponse;
import itx.hazelcast.cluster.server.websocket.WebSocketMessageDispatcher;
import itx.hazelcast.cluster.server.websocket.WsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestRouter implements WsListener {

    private final static Logger LOG = LoggerFactory.getLogger(RequestRouter.class);

    private final static Charset DEF_CHS = Charset.forName("UTF-8");
    private final MessageService messageService;
    private final Map<String, MessageServiceSubscription> subscriptions;
    private WebSocketMessageDispatcher webSocketMessageDispatcher;

    public RequestRouter(MessageService messageService) {
        this.messageService = messageService;
        this.subscriptions = new ConcurrentHashMap<>();
    }

    public void setWebSocketMessageDispatcher(WebSocketMessageDispatcher webSocketMessageDispatcher) {
        this.webSocketMessageDispatcher = webSocketMessageDispatcher;
    }

    @Override
    public void onSessionStart(long sessionId) {

    }

    @Override
    public void onSessionTerminated(long sessionId) {

    }

    @Override
    public void onBinaryMessage(long sessionId, byte[] payload, int offset, int len) {
        MessageWrapper messageWrapper = MessageWrapper.parseFrom(payload);
        onMessageWrapper(sessionId, messageWrapper);
    }

    @Override
    public void onTextMessage(long sessionId, String message) {
        MessageWrapper messageWrapper = MessageWrapper.parseFrom(message.getBytes(DEF_CHS));
        onMessageWrapper(sessionId, messageWrapper);
    }

    private void onMessageWrapper(long sessionId, MessageWrapper messageWrapper) {
        MessageWrapper.PayloadCase payloadCase = messageWrapper.getPayloadCase();
        LOG.info("onMessageWrapper: {} {}", sessionId, payloadCase.name());
        switch(payloadCase.getNumber()) {
            case MessageWrapper.DATAMESSAGE_FIELD_NUMBER:
                DataMessage dataMessage = messageWrapper.getDataMessage();
                LOG.info("onMessage in: {}", dataMessage.getMessage());
                messageService.publishMessage(dataMessage.getTopicId(), dataMessage.getMessage());
                break;
            case MessageWrapper.SUBSCRIBETOTOPICREQUEST_FIELD_NUMBER:
                SubscribeToTopicRequest subscribeToTopicRequest = messageWrapper.getSubscribeToTopicRequest();
                MessageServiceListenerImpl messageServiceListener =
                        new MessageServiceListenerImpl(sessionId, subscribeToTopicRequest.getTopicId(), webSocketMessageDispatcher);
                MessageServiceSubscription messageServiceSubscription =
                        messageService.subscribe(subscribeToTopicRequest.getTopicId(), messageServiceListener);
                messageServiceListener.setSubscriptionId(messageServiceSubscription.getId());
                subscriptions.put(messageServiceSubscription.getId(), messageServiceSubscription);
                SubscribeToTopicResponse subscribeToTopicResponse = SubscribeToTopicResponse.newBuilder()
                        .setRequestId(subscribeToTopicRequest.getRequestId())
                        .setSubscriptionId(messageServiceSubscription.getId())
                        .build();
                MessageWrapper wrapper = MessageWrapper.newBuilder()
                        .setSubscribeToTopicResponse(subscribeToTopicResponse)
                        .build();
                try {
                    webSocketMessageDispatcher.sendBinaryMessage(sessionId, wrapper.toByteArray());
                } catch (IOException e) {
                    LOG.error("Error: ", e);
                }
                break;
            case MessageWrapper.UNSUBSCRIBEFROMTOPICREQUEST_FIELD_NUMBER:
                UnsubscribeFromTopicRequest unsubscribeFromTopicRequest = messageWrapper.getUnsubscribeFromTopicRequest();
                MessageServiceSubscription subscription = subscriptions.get(unsubscribeFromTopicRequest.getSubscriptionId());
                if (subscription != null) {
                    subscriptions.remove(unsubscribeFromTopicRequest.getSubscriptionId());
                    messageService.unSubscribe(subscription);
                }
                UnsubscribeFromTopicResponse unsubscribeFromTopicResponse = UnsubscribeFromTopicResponse.newBuilder()
                        .setRequestId(unsubscribeFromTopicRequest.getRequestId())
                        .build();
                MessageWrapper msgWrapper = MessageWrapper.newBuilder()
                        .setUnsubscribeFromTopicResponse(unsubscribeFromTopicResponse)
                        .build();
                try {
                    webSocketMessageDispatcher.sendBinaryMessage(sessionId, msgWrapper.toByteArray());
                } catch (IOException e) {
                    LOG.error("Error: ", e);
                }
                break;
            default:
                LOG.warn("unsupported message: {}", payloadCase.name());
                break;
        }
    }

    private static class MessageServiceListenerImpl implements MessageServiceListener {

        private final long sessionId;
        private final String topicId;
        private final WebSocketMessageDispatcher webSocketMessageDispatcher;
        private String subscriptionId;

        public MessageServiceListenerImpl(long sessionId, String topicId, WebSocketMessageDispatcher webSocketMessageDispatcher) {
            this.sessionId = sessionId;
            this.topicId = topicId;
            this.webSocketMessageDispatcher = webSocketMessageDispatcher;
        }

        public void setSubscriptionId(String subscriptionId) {
            this.subscriptionId = subscriptionId;
        }

        @Override
        public void onMessage(String message) {
            LOG.info("onMessage out: {}", message);
            try {
                DataMessageEvent dataMessage = DataMessageEvent.newBuilder()
                        .setTopicId(topicId)
                        .setMessage(message)
                        .setSubscriptionId(subscriptionId)
                        .build();
                MessageWrapper messageWrapper = MessageWrapper.newBuilder()
                        .setDataMessageEvent(dataMessage)
                        .build();
                webSocketMessageDispatcher.sendBinaryMessage(sessionId, messageWrapper.toByteArray());
            } catch (IOException e) {
                LOG.error("Error: ", e);
            }
        }

    }

}
