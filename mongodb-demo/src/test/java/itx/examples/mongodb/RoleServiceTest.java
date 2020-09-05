package itx.examples.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import itx.examples.mongodb.dto.Role;
import itx.examples.mongodb.services.DataException;
import itx.examples.mongodb.services.RoleService;
import itx.examples.mongodb.services.RoleServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoleServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceTest.class);

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static RoleService roleService;
    private static Collection<Role> roles;

    @Container
    private static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.2.9");

    @BeforeAll
    public static void init() throws DataException {
        mongoDBContainer.start();
        List<Integer> exposedPorts = mongoDBContainer.getExposedPorts();
        Integer port = exposedPorts.get(0);
        String replicaSetUrl = mongoDBContainer.getReplicaSetUrl();
        Integer boundPort = mongoDBContainer.getMappedPort(port);
        LOG.info("mongodb port {}", port);
        LOG.info("mongodb replicaSetUrl {}", replicaSetUrl);
        LOG.info("mongodb boundPort {}", boundPort);
        mongoClient = Utils.createMongoClient(Utils.getDefaultConnectionString(boundPort));
        database = Utils.createMongoDatabase(mongoClient);
        roleService = new RoleServiceImpl(database);
        roleService.removeAll();
    }

    @Test
    @Order(1)
    public void testRolesEmpty() {
        roles = roleService.getRoles();
        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }

    @Test
    @Order(2)
    public void testInsertFirstRole() throws DataException {
        roleService.insertRole(new Role("1", "aaa"));
        roles = roleService.getRoles();
        assertNotNull(roles);
        assertEquals(1, roles.size());
    }

    @Test
    @Order(3)
    public void testInsertSecondRole() throws DataException {
        roleService.insertRole(new Role("2", "bbb"));
        roles = roleService.getRoles();
        assertNotNull(roles);
        assertEquals(2, roles.size());
    }

    @Test
    @Order(4)
    public void testRemoveFirstRole() throws DataException {
        roleService.removeRole("1");
        roles = roleService.getRoles();
        assertNotNull(roles);
        assertEquals(1, roles.size());
    }

    @Test
    @Order(5)
    public void testRemoveSecondRole() throws DataException {
        roleService.removeRole("2");
        roles = roleService.getRoles();
        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }

    @Test
    @Order(6)
    public void testRolesEmptyFinally() {
        roles = roleService.getRoles();
        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }

    @AfterAll
    public static void destroy() {
        try {
            roleService.removeAll();
        } catch (DataException e) {
            e.printStackTrace();
        }
        mongoClient.close();
    }

}
