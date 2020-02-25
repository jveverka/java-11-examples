package itx.examples.springboot.security.springsecurity.jwt.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import itx.examples.springboot.security.springsecurity.jwt.services.dto.JWToken;
import itx.examples.springboot.security.springsecurity.jwt.services.dto.LoginRequest;
import itx.examples.springboot.security.springsecurity.jwt.services.dto.UserData;

import java.util.Optional;

public interface UserAccessService {

    Optional<UserData> login(LoginRequest loginRequest);

    Optional<Jws<Claims>> isAuthenticated(JWToken jwToken);

    void logout(JWToken jwToken);

}
