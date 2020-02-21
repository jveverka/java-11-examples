package itx.examples.springboot.security.springsecurity.jwt.services.dto;

import java.util.HashSet;
import java.util.Set;

public class UserData {

    private final String userName;
    private final String password;
    private final Set<RoleId> roles;

    public UserData(String userName, String password, Set<RoleId> roles) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    public UserData(String userName, String password, String ... roles) {
        this.userName = userName;
        this.password = password;
        this.roles = new HashSet<>();
        for (String role: roles) {
            this.roles.add(new RoleId(role));
        }
    }

    public String getUserName() {
        return userName;
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    public Set<RoleId> getRoles() {
        return roles;
    }

}
