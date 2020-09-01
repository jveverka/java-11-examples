package itx.examples.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;
import itx.examples.mongodb.dto.Role;
import itx.examples.mongodb.services.DataException;
import itx.examples.mongodb.services.RoleService;
import itx.examples.mongodb.services.RoleServiceImpl;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoleServiceTest {

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static RoleService roleService;

    @Container
    private static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.0");

    @BeforeClass
    public static void init() throws DataException {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        mongoClient = new MongoClient( Utils.SERVER_HOSTNAME, MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        database = mongoClient.getDatabase(Utils.DB_NAME);
        roleService = new RoleServiceImpl(database);
        roleService.removeAll();
    }

    @Test
    public void testRolesService() throws DataException {

        Collection<Role> roles = roleService.getRoles();
        assertNotNull(roles);
        assertTrue(roles.isEmpty());

        roleService.insertRole(new Role("1", "aaa"));
        roles = roleService.getRoles();
        assertNotNull(roles);
        assertTrue(roles.size() == 1);

        roleService.insertRole(new Role("2", "bbb"));
        roles = roleService.getRoles();
        assertNotNull(roles);
        assertTrue(roles.size() == 2);

        roleService.removeRole("1");
        roles = roleService.getRoles();
        assertNotNull(roles);
        assertTrue(roles.size() == 1);

        roleService.removeRole("2");
        roles = roleService.getRoles();
        assertNotNull(roles);
        assertTrue(roles.isEmpty());

    }

    @AfterClass
    public static void destroy() {
        try {
            roleService.removeAll();
        } catch (DataException e) {
            e.printStackTrace();
        }
        mongoClient.close();
    }

}
