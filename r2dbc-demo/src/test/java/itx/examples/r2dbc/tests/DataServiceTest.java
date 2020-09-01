package itx.examples.r2dbc.tests;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import itx.examples.r2dbc.dto.UserData;
import itx.examples.r2dbc.services.DBUtils;
import itx.examples.r2dbc.services.DataService;
import itx.examples.r2dbc.services.DataServiceException;
import itx.examples.r2dbc.services.DataServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(DataServiceTest.class);

    private static ConnectionFactory connectionFactory;
    private static DataService dataService;
    private static Map<String, UserData> users;

    // will be shared between test methods
    @Container
    private static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:12.3")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("secret");

    @BeforeAll
    public static void init() throws DataServiceException {
        List<Integer> exposedPorts = postgresqlContainer.getExposedPorts();
        Integer port = exposedPorts.get(0);
        String jdbcUrl = postgresqlContainer.getJdbcUrl();
        Integer boundPort = postgresqlContainer.getMappedPort(port);
        LOG.info("postgresql port {}", port);
        LOG.info("postgresql jdbcUrl {}", jdbcUrl);
        LOG.info("postgresql boundPort {}", boundPort);
        connectionFactory = ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, "postgresql")
                .option(ConnectionFactoryOptions.HOST, "localhost")
                .option(ConnectionFactoryOptions.PORT, boundPort)
                .option(ConnectionFactoryOptions.USER, "testuser")
                .option(ConnectionFactoryOptions.PASSWORD, "secret")
                .option(ConnectionFactoryOptions.DATABASE, "testdb")
                .build());
        assertTrue(postgresqlContainer.isRunning());
        DBUtils.initDb(connectionFactory);
        dataService = new DataServiceImpl(connectionFactory);
        users = new HashMap<>();
    }

    @Test
    @Order(2)
    public void testInitialData() throws DataServiceException {
        Collection<UserData> userDataCollection = dataService.findAll();
        assertNotNull(userDataCollection);
        assertTrue(userDataCollection.isEmpty());
    }


    @Test
    @Order(3)
    public void testInsertData() throws DataServiceException {
        UserData userData = dataService.save("john@gmail.com", "secret");
        assertNotNull(userData);
        assertNotNull(userData.getId());
        assertEquals("john@gmail.com", userData.getEmail());
        assertEquals("secret", userData.getPassword());
        users.put(userData.getId(), userData);
        Collection<UserData> userDataCollection = dataService.findAll();
        assertNotNull(userDataCollection);
        assertEquals(1, userDataCollection.size());
    }

    @Test
    @Order(4)
    public void testFetchData() throws DataServiceException {
        Optional<String> userId = users.keySet().stream().findFirst();
        assertTrue(userId.isPresent());
        Optional<UserData> userData = dataService.findById(userId.get());
        assertNotNull(userData);
        assertTrue(userData.isPresent());
        assertEquals(userId.get(), userData.get().getId());
        assertEquals("john@gmail.com", userData.get().getEmail());
        assertEquals("secret", userData.get().getPassword());
    }

    @Test
    @Order(5)
    public void testInsertMoreData() throws DataServiceException {
        UserData userData = dataService.save("jane@gmail.com", "super");
        assertNotNull(userData);
        assertNotNull(userData.getId());
        users.put(userData.getId(), userData);
        Collection<UserData> userDataCollection = dataService.findAll();
        assertNotNull(userDataCollection);
        assertEquals(2, userDataCollection.size());
    }

    @Test
    @Order(6)
    public void updateData() throws DataServiceException {
        Optional<String> userId = users.keySet().stream().findFirst();
        assertTrue(userId.isPresent());
        Optional<UserData> userData = dataService.findById(userId.get());
        assertNotNull(userData);
        assertTrue(userData.isPresent());
        UserData userDataUpdated = new UserData(userData.get().getId(), userData.get().getEmail(), "top-secret");
        dataService.update(userDataUpdated);
        userData = dataService.findById(userId.get());
        assertNotNull(userData);
        assertTrue(userData.isPresent());
        assertEquals("top-secret", userData.get().getPassword());
    }

    @Test
    @Order(7)
    public void testDeleteFirstUser() throws DataServiceException {
        Optional<String> userId = users.keySet().stream().findFirst();
        assertTrue(userId.isPresent());
        dataService.deleteById(userId.get());
        Collection<UserData> userDataCollection = dataService.findAll();
        assertNotNull(userDataCollection);
        assertEquals(1, userDataCollection.size());
        users.remove(userId.get());
    }

    @Test
    @Order(8)
    public void testDeleteSecondUser() throws DataServiceException {
        Optional<String> userId = users.keySet().stream().findFirst();
        assertTrue(userId.isPresent());
        dataService.deleteById(userId.get());
        Collection<UserData> userDataCollection = dataService.findAll();
        assertNotNull(userDataCollection);
        assertEquals(0, userDataCollection.size());
        users.remove(userId.get());
    }

    @Test
    @Order(9)
    public void testRemainingData() throws DataServiceException {
        Collection<UserData> userDataCollection = dataService.findAll();
        assertNotNull(userDataCollection);
        assertTrue(userDataCollection.isEmpty());
    }

    @Test
    @Order(10)
    public void findNonExistingData() throws DataServiceException {
        Optional<UserData> userData = dataService.findById("invalid-id");
        assertNotNull(userData);
        assertTrue(userData.isEmpty());
    }

    @Test
    @Order(11)
    public void deleteNonExistingData() {
        Exception exception = assertThrows(DataServiceException.class, () -> {
            dataService.deleteById("invalid-id");
        });
    }

    @Test
    @Order(12)
    public void updateNonExistingData() throws Exception {
        Exception exception = assertThrows(DataServiceException.class, () -> {
            dataService.update(new UserData("invalid-id", "email", "password"));
        });
    }

}
