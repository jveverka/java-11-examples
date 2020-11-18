package itx.examples.redis.tests;

import itx.examples.redis.dto.UserData;
import itx.examples.redis.services.DataService;
import itx.examples.redis.services.DataServiceException;
import itx.examples.redis.services.DataServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.GenericContainer;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataServiceTest {

    private static GenericContainer<?> redisContainer = new GenericContainer<>("redis:6-alpine")
            .withExposedPorts(6379)
            .withReuse(true);

    private static DataService dataService;
    private static String firstId;
    private static String secondId;

    @BeforeAll
    public static void init() {
        redisContainer.start();
        Assertions.assertTrue(redisContainer.isRunning());
        String host = redisContainer.getContainerIpAddress();
        int port = redisContainer.getMappedPort(6379);
        dataService = new DataServiceImpl(host, port);
    }

    @Test
    @Order(1)
    public void testServiceAfterInit() throws DataServiceException {
        Collection<UserData> userDataCollection = dataService.findAll();
        assertNotNull(userDataCollection);
        assertEquals(0, userDataCollection.size());
    }

    @Test
    @Order(2)
    public void insertFirstRecord() throws DataServiceException {
        UserData userData = dataService.save("Juraj", "secret");
        assertNotNull(userData);
        assertNotNull(userData.getId());
        assertEquals("Juraj", userData.getEmail());
        assertEquals("secret", userData.getPassword());
        firstId = userData.getId();
    }

    @Test
    @Order(3)
    public void checkAfterFirstInsert() throws DataServiceException {
        Collection<UserData> userDataCollection = dataService.findAll();
        assertNotNull(userDataCollection);
        assertEquals(1, userDataCollection.size());

        Optional<UserData> userData = dataService.findById(firstId);
        assertTrue(userData.isPresent());
        assertEquals(firstId, userData.get().getId());
    }

    @Test
    @Order(4)
    public void insertSecondRecord() throws DataServiceException {
        UserData userData = dataService.save("Martina", "secret");
        assertNotNull(userData);
        assertNotNull(userData.getId());
        assertEquals("Martina", userData.getEmail());
        assertEquals("secret", userData.getPassword());
        secondId = userData.getId();
    }

    @Test
    @Order(5)
    public void checkAfterSecondInsert() throws DataServiceException {
        Collection<UserData> userDataCollection = dataService.findAll();
        assertNotNull(userDataCollection);
        assertEquals(2, userDataCollection.size());

        Optional<UserData> userData = dataService.findById(secondId);
        assertTrue(userData.isPresent());
        assertEquals(secondId, userData.get().getId());
    }

    @Test
    @Order(6)
    public void removeFirst() throws DataServiceException {
        dataService.deleteById(firstId);
        Collection<UserData> userDataCollection = dataService.findAll();
        assertNotNull(userDataCollection);
        assertEquals(1, userDataCollection.size());

        Optional<UserData> userData = dataService.findById(firstId);
        assertTrue(userData.isEmpty());
        userData = dataService.findById(secondId);
        assertTrue(userData.isPresent());
        assertEquals(secondId, userData.get().getId());
    }

    @Test
    @Order(7)
    public void removeSecond() throws DataServiceException {
        dataService.deleteById(secondId);
        Collection<UserData> userDataCollection = dataService.findAll();
        assertNotNull(userDataCollection);
        assertEquals(0, userDataCollection.size());

        Optional<UserData> userData = dataService.findById(firstId);
        assertTrue(userData.isEmpty());
        userData = dataService.findById(secondId);
        assertTrue(userData.isEmpty());
    }

    @Test
    @Order(8)
    public void lastCheck() throws DataServiceException {
        Collection<UserData> userDataCollection = dataService.findAll();
        assertNotNull(userDataCollection);
        assertEquals(0, userDataCollection.size());
    }

}
