package itx.examples.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import itx.examples.mongodb.dto.Address;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.mongojack.JacksonMongoCollection;

public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("Do not instantiate utility class.");
    }

    public static final String SERVER_HOSTNAME = "localhost";
    public static final String DB_NAME = "test";
    public static final String ROLES_COLLECTION_NAME = "roles";
    public static final String ADDRESSES_COLLECTION_NAME = "addresses";
    public static final int DEFAULT_PORT = 27017;

    public static String getDefaultConnectionString() {
        return "mongodb://" + SERVER_HOSTNAME + ":" + DEFAULT_PORT;
    }

    public static String getDefaultConnectionString(int port) {
        return "mongodb://" + SERVER_HOSTNAME + ":" + port;
    }

    public static MongoClient createMongoClient(String connectionString) {
        return MongoClients.create(connectionString);
    }

    public static MongoDatabase createMongoDatabase(MongoClient mongoClient) {
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        return mongoClient.getDatabase(Utils.DB_NAME).withCodecRegistry(pojoCodecRegistry);
    }

    public static JacksonMongoCollection<Address> createJacksonMongoCollection(MongoClient mongoClient) {
        return JacksonMongoCollection.builder()
                .build(mongoClient, DB_NAME, ADDRESSES_COLLECTION_NAME, Address.class, UuidRepresentation.JAVA_LEGACY);
    }

}
