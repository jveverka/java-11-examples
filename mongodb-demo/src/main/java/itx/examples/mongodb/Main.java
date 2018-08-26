package itx.examples.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;
import itx.examples.mongodb.dto.Role;
import itx.examples.mongodb.services.RoleService;
import itx.examples.mongodb.services.RoleServiceImpl;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Main {

    final private static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args   ) {
        LOG.info("MongoDB demo starting ...");
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClient mongoClient = new MongoClient( Utils.SERVER_HOSTNAME, MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        MongoDatabase database = mongoClient.getDatabase(Utils.DB_NAME);
        RoleService roleService = new RoleServiceImpl(database);
        Collection<Role> roles = roleService.getRoles();
        LOG.info("Roles: {}", roles.size());
        LOG.info("MongoDB demo done.");
    }

}
