package itx.examples.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import itx.examples.mongodb.dto.Address;
import itx.examples.mongodb.dto.Role;
import itx.examples.mongodb.services.AddressService;
import itx.examples.mongodb.services.AddressServiceImpl;
import itx.examples.mongodb.services.DataException;
import itx.examples.mongodb.services.RoleService;
import itx.examples.mongodb.services.RoleServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mongojack.JacksonMongoCollection;
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
public class AddressServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(AddressServiceTest.class);

    private static MongoClient mongoClient;
    private static AddressService addressService;
    private static Collection<Address> addresses;

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
        JacksonMongoCollection<Address> jacksonMongoCollection = Utils.createJacksonMongoCollection(mongoClient);
        addressService = new AddressServiceImpl(jacksonMongoCollection);
        addressService.removeAll();
    }

    @Test
    @Order(1)
    public void testAddressesEmpty() {
        addresses = addressService.getAll();
        assertNotNull(addresses);
        assertTrue(addresses.isEmpty());
    }

    @Test
    @Order(2)
    public void testInsertFirstAddress() throws DataException {
        addressService.insert(new Address("1", "aaa", "bbb"));
        addresses = addressService.getAll();
        assertNotNull(addresses);
        assertEquals(1, addresses.size());
    }

    @Test
    @Order(3)
    public void testInsertSecondAddress() throws DataException {
        addressService.insert(new Address("2", "bbb", "ddd"));
        addresses = addressService.getAll();
        assertNotNull(addresses);
        assertEquals(2, addresses.size());
    }

    @Test
    @Order(4)
    public void testFindById() throws DataException {
        Address address = addressService.get("1");
        assertNotNull(address);
        assertEquals("1", address.getId());
        assertEquals("aaa", address.getStreet());
        assertEquals("bbb", address.getCity());

        address = addressService.get("2");
        assertNotNull(address);
        assertEquals("2", address.getId());
        assertEquals("bbb", address.getStreet());
        assertEquals("ddd", address.getCity());
    }

    @Test
    @Order(5)
    public void testUpdate() throws DataException {
        addressService.update(new Address("1", "zzz", "yyy"));
        Address address = addressService.get("1");
        assertNotNull(address);
        assertEquals("1", address.getId());
        assertEquals("zzz", address.getStreet());
        assertEquals("yyy", address.getCity());
    }

    @Test
    @Order(6)
    public void testRemoveFirstAddress() throws DataException {
        addressService.remove("1");
        addresses = addressService.getAll();
        assertNotNull(addresses);
        assertEquals(1, addresses.size());
    }

    @Test
    @Order(7)
    public void testRemoveSecondAddress() throws DataException {
        addressService.remove("2");
        addresses = addressService.getAll();
        assertNotNull(addresses);
        assertTrue(addresses.isEmpty());
    }

    @Test
    @Order(8)
    public void testAddressesEmptyFinally() {
        addresses = addressService.getAll();
        assertNotNull(addresses);
        assertTrue(addresses.isEmpty());
    }

    @AfterAll
    public static void destroy() {
        try {
            addressService.removeAll();
        } catch (DataException e) {
            e.printStackTrace();
        }
        mongoClient.close();
    }

}
