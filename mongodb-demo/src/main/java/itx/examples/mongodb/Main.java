package itx.examples.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import itx.examples.mongodb.dto.Role;
import itx.examples.mongodb.services.RoleService;
import itx.examples.mongodb.services.RoleServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args   ) {
        LOG.info("MongoDB demo starting ...");
        MongoClient mongoClient = Utils.createMongoClient(Utils.getDefaultConnectionString());
        MongoDatabase database = Utils.createMongoDatabase(mongoClient);
        RoleService roleService = new RoleServiceImpl(database);
        Collection<Role> roles = roleService.getAll();
        LOG.info("Roles: {}", roles.size());
        LOG.info("MongoDB demo done.");
    }

}
