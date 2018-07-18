package itx.examples.jetty.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageId {

    private Long timeStamp;
    private ConversationId conversationId;

    @JsonCreator
    public MessageId(@JsonProperty("timeStamp") Long timeStamp,
                     @JsonProperty("conversationId") ConversationId conversationId) {
        this.timeStamp = timeStamp;
        this.conversationId = conversationId;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public ConversationId getConversationId() {
        return conversationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageId messageId = (MessageId) o;

        if (!timeStamp.equals(messageId.timeStamp)) return false;
        return conversationId.equals(messageId.conversationId);
    }

    @Override
    public int hashCode() {
        int result = timeStamp.hashCode();
        result = 31 * result + conversationId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MessageId{" +
                "timeStamp=" + timeStamp +
                ", conversationId=" + conversationId +
                '}';
    }

}
