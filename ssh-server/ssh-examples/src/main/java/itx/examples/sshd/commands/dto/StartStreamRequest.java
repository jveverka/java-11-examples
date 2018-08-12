package itx.examples.sshd.commands.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
public class StartStreamRequest {

    private long id;
    private long streamCount;
    private String message;

    @JsonCreator
    public StartStreamRequest(@JsonProperty("id") long id,
                              @JsonProperty("streamCount") long streamCount,
                              @JsonProperty("message") String message) {
        this.id = id;
        this.streamCount = streamCount;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public long getStreamCount() {
        return streamCount;
    }

    public String getMessage() {
        return message;
    }

}
