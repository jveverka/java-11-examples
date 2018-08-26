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
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collection;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class RoleServiceITTest {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private RoleService roleService;

    @BeforeClass
    public void init() throws DataException {
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
        Assert.assertNotNull(roles);
        Assert.assertTrue(roles.isEmpty());

        roleService.insertRole(new Role("1", "aaa"));
        roles = roleService.getRoles();
        Assert.assertNotNull(roles);
        Assert.assertTrue(roles.size() == 1);

        roleService.insertRole(new Role("2", "bbb"));
        roles = roleService.getRoles();
        Assert.assertNotNull(roles);
        Assert.assertTrue(roles.size() == 2);

        roleService.removeRole("1");
        roles = roleService.getRoles();
        Assert.assertNotNull(roles);
        Assert.assertTrue(roles.size() == 1);

        roleService.removeRole("2");
        roles = roleService.getRoles();
        Assert.assertNotNull(roles);
        Assert.assertTrue(roles.isEmpty());

    }

    @AfterClass
    public void destroy() {
        try {
            roleService.removeAll();
        } catch (DataException e) {
            e.printStackTrace();
        }
        mongoClient.close();
    }

}
