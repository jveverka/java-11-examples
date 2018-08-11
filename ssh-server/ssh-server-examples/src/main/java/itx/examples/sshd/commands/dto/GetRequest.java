package itx.examples.sshd.commands.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
public class GetRequest {

    private long id;

    @JsonCreator
    public GetRequest(@JsonProperty("id") long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
