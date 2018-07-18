package itx.examples.jetty.common;

import itx.examples.jetty.common.dto.ConversationId;
import itx.examples.jetty.common.dto.Message;
import itx.examples.jetty.common.dto.MessageId;

public final class MessageUtils {

    private MessageUtils() {
    }

    public static Message createMessage(Long timeStamp, String userId, String groupId, String message) {
        ConversationId conversationId = new ConversationId(userId, groupId);
        MessageId messageId = new MessageId(timeStamp, conversationId);
        return new Message(messageId, message);
    }

    public static Message createMessage(String userId, String groupId, String message) {
        ConversationId conversationId = new ConversationId(userId, groupId);
        MessageId messageId = new MessageId(System.currentTimeMillis(), conversationId);
        return new Message(messageId, message);
    }

}
