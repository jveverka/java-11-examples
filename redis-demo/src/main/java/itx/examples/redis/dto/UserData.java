package itx.examples.redis.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserData {

    private final String id;
    private final String email;
    private final String password;

    @JsonCreator
    public UserData(@JsonProperty("id") String id,
                    @JsonProperty("email") String email,
                    @JsonProperty("password") String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
