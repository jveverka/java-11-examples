package itx.examples.springboot.security.springsecurity.jwt.services;


import itx.examples.springboot.security.springsecurity.jwt.services.dto.LoginRequest;
import itx.examples.springboot.security.springsecurity.jwt.services.dto.UserData;

import java.util.Optional;

public interface UserAccessService {

    Optional<UserData> login(String sessionId, LoginRequest loginRequest);

    Optional<UserData> isAuthenticated(String sessionId);

    void logout(String sessionId);

}
