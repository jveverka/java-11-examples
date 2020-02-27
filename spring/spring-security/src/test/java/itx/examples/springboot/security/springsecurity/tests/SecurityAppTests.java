package itx.examples.springboot.security.springsecurity.tests;

import itx.examples.springboot.security.springsecurity.services.dto.ServerData;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityAppTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    public void getPublicData() throws MalformedURLException {
        ResponseEntity<ServerData> response = restTemplate.getForEntity(
                new URL("http://localhost:" + port + "/services/public/data/all").toString(), ServerData.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ServerData serverData = response.getBody();
        assertNotNull(serverData);
        assertNotNull(serverData.getData());
        assertEquals("Public", serverData.getSource());
        assertTrue(serverData.getTimestamp() > 0);
    }

}
