package itx.examples.springboot.security.springsecurity.jwt.services;

import itx.examples.springboot.security.springsecurity.jwt.services.dto.JWToken;
import itx.examples.springboot.security.springsecurity.jwt.services.dto.LoginRequest;
import itx.examples.springboot.security.springsecurity.jwt.services.dto.UserData;
import itx.examples.springboot.security.springsecurity.jwt.services.dto.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserAccessServiceImpl implements UserAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(UserAccessServiceImpl.class);

    private final Map<String, UserData> users;

    public UserAccessServiceImpl() {
        this.users = new ConcurrentHashMap<>();
        this.users.put("joe", new UserData(UserId.from("joe"), "secret", "ROLE_USER"));
        this.users.put("jane", new UserData(UserId.from("jane"), "secret", "ROLE_ADMIN", "ROLE_USER"));
        this.users.put("alice", new UserData(UserId.from("joe"), "secret", "ROLE_PUBLIC"));
    }

    @Override
    public Optional<UserData> login(LoginRequest loginRequest) {
        return Optional.empty();
    }

    @Override
    public Optional<UserData> isAuthenticated(JWToken jwToken) {
        return Optional.empty();
    }

    @Override
    public void logout(JWToken jwToken) {
        LOG.info("logout: {}", jwToken.getToken());
    }

}
