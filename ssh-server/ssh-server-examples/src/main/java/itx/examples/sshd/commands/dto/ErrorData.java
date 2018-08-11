package itx.examples.sshd.commands.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
public class ErrorData {

    private String message;

    @JsonCreator
    public ErrorData(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
