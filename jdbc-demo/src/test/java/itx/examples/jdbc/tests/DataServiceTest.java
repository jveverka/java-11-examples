package itx.examples.jdbc.tests;

import itx.examples.jdbc.dto.UserData;
import itx.examples.jdbc.services.DBUtils;
import itx.examples.jdbc.services.DataService;
import itx.examples.jdbc.services.DataServiceException;
import itx.examples.jdbc.services.DataServiceImpl;
import org.junit.jupiter.api.Assertions;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(DataServiceTest.class);

    private static Connection connection;
    private static DataService dataService;
    private static Map<String, UserData> users;

    // will be shared between test methods
    @Container
    private static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:12.3")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("secret");

    @BeforeAll
    public static void init() throws DataServiceException, SQLException {
        assertTrue(postgresqlContainer.isRunning());
        String jdbcUrl = postgresqlContainer.getJdbcUrl();
        LOG.info("jdbcUrl: {}", jdbcUrl);
        assertNotNull(jdbcUrl);
        connection = DriverManager.getConnection(jdbcUrl,"testuser","secret");
        assertTrue(connection.isValid(2000));
        DBUtils.initDb(connection);
        dataService = new DataServiceImpl(connection);
        users = new HashMap<>();
    }

    @Test
    @Order(2)
    public void testInitialData() throws DataServiceException {
        Collection<UserData> userDataCollection = dataService.findAll();
        Assertions.assertNotNull(userDataCollection);
        assertTrue(userDataCollection.isEmpty());
    }


    @Test
    @Order(3)
    public void testInsertData() throws DataServiceException {
        UserData userData = dataService.save("john@gmail.com", "secret");
        Assertions.assertNotNull(userData);
        Assertions.assertNotNull(userData.getId());
        assertEquals("john@gmail.com", userData.getEmail());
        assertEquals("secret", userData.getPassword());
        users.put(userData.getId(), userData);
        Collection<UserData> userDataCollection = dataService.findAll();
        Assertions.assertNotNull(userDataCollection);
        assertEquals(1, userDataCollection.size());
    }

    @Test
    @Order(4)
    public void testFetchData() throws DataServiceException {
        Optional<String> userId = users.keySet().stream().findFirst();
        assertTrue(userId.isPresent());
        Optional<UserData> userData = dataService.findById(userId.get());
        Assertions.assertNotNull(userData);
        assertTrue(userData.isPresent());
        assertEquals(userId.get(), userData.get().getId());
        assertEquals("john@gmail.com", userData.get().getEmail());
        assertEquals("secret", userData.get().getPassword());
    }

    @Test
    @Order(5)
    public void testInsertMoreData() throws DataServiceException {
        UserData userData = dataService.save("jane@gmail.com", "super");
        Assertions.assertNotNull(userData);
        Assertions.assertNotNull(userData.getId());
        users.put(userData.getId(), userData);
        Collection<UserData> userDataCollection = dataService.findAll();
        Assertions.assertNotNull(userDataCollection);
        assertEquals(2, userDataCollection.size());
    }

    @Test
    @Order(6)
    public void updateData() throws DataServiceException {
        Optional<String> userId = users.keySet().stream().findFirst();
        assertTrue(userId.isPresent());
        Optional<UserData> userData = dataService.findById(userId.get());
        Assertions.assertNotNull(userData);
        assertTrue(userData.isPresent());
        UserData userDataUpdated = new UserData(userData.get().getId(), userData.get().getEmail(), "top-secret");
        dataService.update(userDataUpdated);
        userData = dataService.findById(userId.get());
        Assertions.assertNotNull(userData);
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
        Assertions.assertNotNull(userDataCollection);
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
        Assertions.assertNotNull(userDataCollection);
        assertEquals(0, userDataCollection.size());
        users.remove(userId.get());
    }

    @Test
    @Order(9)
    public void testRemainingData() throws DataServiceException {
        Collection<UserData> userDataCollection = dataService.findAll();
        Assertions.assertNotNull(userDataCollection);
        assertTrue(userDataCollection.isEmpty());
    }

    @Test
    @Order(10)
    public void findNonExistingData() throws DataServiceException {
        Optional<UserData> userData = dataService.findById("invalid-id");
        Assertions.assertNotNull(userData);
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
