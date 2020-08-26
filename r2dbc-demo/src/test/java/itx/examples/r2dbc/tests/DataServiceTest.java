package itx.examples.r2dbc.tests;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import itx.examples.r2dbc.services.DataService;
import itx.examples.r2dbc.services.DataServiceImpl;
import org.junit.jupiter.api.AfterAll;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(DataServiceTest.class);

    private static ConnectionFactory connectionFactory;
    private static DataService dataService;

    // will be shared between test methods
    @Container
    private static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("secret");

    @BeforeAll
    public static void init() {
        List<Integer> exposedPorts = postgresqlContainer.getExposedPorts();
        Integer port = exposedPorts.get(0);
        LOG.info("postgresql port {}", port);
        connectionFactory = ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, "postgresql")
                .option(ConnectionFactoryOptions.HOST, "localhost")
                .option(ConnectionFactoryOptions.PORT, port)
                .option(ConnectionFactoryOptions.USER, "testuser")
                .option(ConnectionFactoryOptions.PASSWORD, "secret")
                .option(ConnectionFactoryOptions.DATABASE, "testdb")
                .build());
        dataService = new DataServiceImpl(connectionFactory);
    }

    @AfterAll
    public static void shutdown() throws Exception {
        dataService.close();
    }

    @Test
    @Order(1)
    public void testContainerRunning() {
        assertTrue(postgresqlContainer.isRunning());
    }

}
