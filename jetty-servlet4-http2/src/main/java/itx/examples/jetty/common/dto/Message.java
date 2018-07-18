package itx.examples.jetty.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

    private MessageId id;
    private String message;

    @JsonCreator
    public Message(@JsonProperty("id") MessageId id,
                   @JsonProperty("message") String message) {
        this.id = id;
        this.message = message;
    }

    public MessageId getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (!id.equals(message1.id)) return false;
        return message.equals(message1.message);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }

}
