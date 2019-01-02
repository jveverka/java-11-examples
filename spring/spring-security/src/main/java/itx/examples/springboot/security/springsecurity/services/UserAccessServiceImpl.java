package itx.examples.springboot.security.springsecurity.services;

import itx.examples.springboot.security.springsecurity.services.dto.LoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserAccessServiceImpl implements UserAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(UserAccessServiceImpl.class);

    private final Map<String, LoginRequest> sessions;
    private final Map<String, String> users;

    public UserAccessServiceImpl() {
        this.sessions = new ConcurrentHashMap<>();
        this.users = new HashMap<>();
        this.users.put("joe", "secret");
        this.users.put("jane", "secret");
    }

    @Override
    public boolean login(String sessionId, LoginRequest loginRequest) {
        String password = users.get(loginRequest.getUserName());
        if (password != null && password.equals(loginRequest.getPassword())) {
            LOG.info("login OK: {} {}", sessionId, loginRequest.getUserName());
            sessions.put(sessionId, loginRequest);
            return true;
        }
        LOG.info("login Failed: {} {}", sessionId, loginRequest.getUserName());
        return false;
    }

    @Override
    public boolean isAuthorized(String sessionId) {
        return sessions.get(sessionId)!=null ? true : false;
    }

    @Override
    public void logout(String sessionId) {
        LOG.info("logout: {} {}", sessionId);
        sessions.remove(sessionId);
    }

}
