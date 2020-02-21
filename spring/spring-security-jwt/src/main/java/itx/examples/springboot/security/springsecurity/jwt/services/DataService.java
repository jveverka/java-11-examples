package itx.examples.springboot.security.springsecurity.jwt.services;


import itx.examples.springboot.security.springsecurity.jwt.services.dto.ServerData;

public interface DataService {

    ServerData getSecuredData(String source);

}
