package itx.examples.sshd.commands.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
public class StreamResponse {

    private long id;
    private long ordinal;
    private String message;

    @JsonCreator
    public StreamResponse(@JsonProperty("id") long id,
                          @JsonProperty("ordinal") long ordinal,
                          @JsonProperty("message") String message) {
        this.id = id;
        this.ordinal = ordinal;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public long getOrdinal() {
        return ordinal;
    }

    public String getMessage() {
        return message;
    }

}
