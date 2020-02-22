package itx.examples.springboot.security.springsecurity.jwt.services.dto;

import java.util.HashSet;
import java.util.Set;

public class UserData {

    private final UserId userId;
    private final Set<RoleId> roles;
    private final JWToken jwToken;

    public UserData(UserId userId, JWToken jwToken, Set<RoleId> roles) {
        this.userId = userId;
        this.roles = roles;
        this.jwToken = jwToken;
    }

    public UserData(UserId userId, JWToken jwToken, String ... roles) {
        this.userId = userId;
        this.roles = new HashSet<>();
        for (String role: roles) {
            this.roles.add(new RoleId(role));
        }
        this.jwToken = jwToken;
    }

    public UserData(UserId userId, String ... roles) {
        this.userId = userId;
        this.roles = new HashSet<>();
        for (String role: roles) {
            this.roles.add(new RoleId(role));
        }
        this.jwToken = null;
    }

    public UserId getUserId() {
        return userId;
    }

    public Set<RoleId> getRoles() {
        return roles;
    }

    public JWToken getJwToken() {
        return jwToken;
    }

}
