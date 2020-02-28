package itx.examples.springboot.security.springsecurity.services.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionId {

    private final String id;

    @JsonCreator
    public SessionId(@JsonProperty("id") String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static SessionId from(String sessionId) {
        return new SessionId(sessionId);
    }

}
