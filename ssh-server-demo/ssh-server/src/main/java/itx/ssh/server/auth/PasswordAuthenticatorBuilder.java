package itx.ssh.server.auth;

import org.apache.sshd.server.auth.password.PasswordAuthenticator;

import java.util.HashMap;
import java.util.Map;

public class PasswordAuthenticatorBuilder {

    private Map<String, String> credentials;

    public PasswordAuthenticatorBuilder() {
        this.credentials = new HashMap<>();
    }

    public PasswordAuthenticatorBuilder addCredentials(String username, String password) {
        this.credentials.put(username, password);
        return this;
    }

    public PasswordAuthenticator build() {
        return new PasswordAuthenticatorImpl(credentials);
    }

}
