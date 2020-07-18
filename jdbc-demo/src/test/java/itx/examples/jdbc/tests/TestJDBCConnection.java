package itx.examples.jdbc.tests;

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

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestJDBCConnection {

    private static final Logger LOG = LoggerFactory.getLogger(TestJDBCConnection.class);

    // will be shared between test methods
    @Container
    private static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("secret");

    @Test
    @Order(1)
    public void testContainerRunning() {
        assertTrue(postgresqlContainer.isRunning());
    }

    @Test
    @Order(2)
    public void testJDBCConnection() throws SQLException {
        String jdbcUrl = postgresqlContainer.getJdbcUrl();
        LOG.info("jdbcUrl: {}", jdbcUrl);
        assertNotNull(jdbcUrl);
        Connection connection = DriverManager.getConnection(jdbcUrl,"testuser","secret");
        assertTrue(connection.isValid(2000));
        connection.close();
    }

}
