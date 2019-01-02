package itx.examples.springboot.security.springsecurity.services;

import itx.examples.springboot.security.springsecurity.services.dto.LoginRequest;

public interface UserAccessService {

    boolean login(String sessionId, LoginRequest loginRequest);

    boolean isAuthorized(String sessionId);

    void logout(String sessionId);

}
