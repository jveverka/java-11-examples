package itx.ssh.server.auth;

import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.session.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PasswordAuthenticatorImpl implements PasswordAuthenticator {

    final private static Logger LOG = LoggerFactory.getLogger(PasswordAuthenticatorImpl.class);

    private Map<String, String> credentials;

    public PasswordAuthenticatorImpl(Map<String, String> credentials) {
       this.credentials = credentials;
    }

    @Override
    public boolean authenticate(String username, String password, ServerSession session) throws PasswordChangeRequiredException, AsyncAuthException {
        LOG.info("authenticate: {}", username);
        String passwd = credentials.get(username);
        if (passwd != null && passwd.equals(passwd)) {
            return true;
        } else {
            return false;
        }
    }

}
