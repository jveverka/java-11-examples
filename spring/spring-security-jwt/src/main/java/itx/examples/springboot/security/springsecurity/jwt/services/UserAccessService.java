package itx.examples.springboot.security.springsecurity.jwt.services;


import itx.examples.springboot.security.springsecurity.jwt.services.dto.JWToken;
import itx.examples.springboot.security.springsecurity.jwt.services.dto.LoginRequest;
import itx.examples.springboot.security.springsecurity.jwt.services.dto.UserData;

import java.util.Optional;

public interface UserAccessService {

    Optional<UserData> login(LoginRequest loginRequest);

    Optional<UserData> isAuthenticated(JWToken jwToken);

    void logout(JWToken jwToken);

}
