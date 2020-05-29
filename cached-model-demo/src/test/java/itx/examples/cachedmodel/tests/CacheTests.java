package itx.examples.cachedmodel.tests;

import itx.examples.cachedmodel.ServiceBuilder;
import itx.examples.cachedmodel.model.Address;
import itx.examples.cachedmodel.model.AddressId;
import itx.examples.cachedmodel.model.Model;
import itx.examples.cachedmodel.model.ModelId;
import itx.examples.cachedmodel.model.Organization;
import itx.examples.cachedmodel.model.OrganizationId;
import itx.examples.cachedmodel.model.User;
import itx.examples.cachedmodel.model.UserId;
import itx.examples.cachedmodel.services.AddressManager;
import itx.examples.cachedmodel.services.ModelManager;
import itx.examples.cachedmodel.services.OrganizationManager;
import itx.examples.cachedmodel.services.UserManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CacheTests {

    private static ServiceBuilder.Services services;

    private static ModelId model100;
    private static AddressId address110;
    private static OrganizationId organization110;
    private static UserId user111;

    @BeforeAll
    public static void init() {
        services = ServiceBuilder.builder()
                .writeThroughCache()
                .build();
    }

    @Test
    @Order(1)
    public void addModelTest() {
        ModelManager modelManager = services.getModelManager();
        model100 = modelManager.add("model-100");
        assertNotNull(model100);
        ModelId model002 = modelManager.add("model-200");
        Collection<Model> models = modelManager.getAll();
        assertNotNull(models);
        assertEquals(2, models.size());
        modelManager.remove(model002);
        models = modelManager.getAll();
        assertEquals(1, models.size());
    }

    @Test
    @Order(2)
    public void addAddressTest() {
        AddressManager addressManager = services.getAddressManager();
        Optional<AddressId> addressIdOptional = addressManager.add(model100, "WS", "NY");
        assertTrue(addressIdOptional.isPresent());
        address110 = addressIdOptional.get();
        Collection<Address> addresses = addressManager.getAll(model100);
        assertEquals(1, addresses.size());
        addressIdOptional = addressManager.add(model100, "HT", "NY");
        assertTrue(addressIdOptional.isPresent());
        addresses = addressManager.getAll(model100);
        assertEquals(2, addresses.size());
        addressManager.remove(model100, addressIdOptional.get());
        addresses = addressManager.getAll(model100);
        assertEquals(1, addresses.size());
    }

    @Test
    @Order(3)
    public void addOrganizationTest() {
        OrganizationManager organizationManager = services.getOrganizationManager();
        Optional<OrganizationId> organizationIdOptional = organizationManager.add(model100, "organization-110", address110);
        assertTrue(organizationIdOptional.isPresent());
        organization110 = organizationIdOptional.get();
        Collection<Organization> organizations = organizationManager.getAll(model100);
        assertEquals(1, organizations.size());
        organizationIdOptional = organizationManager.add(model100, "organization-120", address110);
        organizations = organizationManager.getAll(model100);
        assertEquals(2, organizations.size());
        organizationManager.remove(model100, organizationIdOptional.get());
        organizations = organizationManager.getAll(model100);
        assertEquals(1, organizations.size());
    }

    @Test
    @Order(4)
    public void addUserTest() {
        UserManager userManager = services.getUserManager();
        Optional<UserId> userIdOptional = userManager.add(model100, organization110, "user-111", address110);
        assertTrue(userIdOptional.isPresent());
        user111 = userIdOptional.get();
        Collection<User> users = userManager.getAll(model100, organization110);
        assertEquals(1, users.size());
        userIdOptional = userManager.add(model100, organization110, "user-112", address110);
        assertTrue(userIdOptional.isPresent());
        users = userManager.getAll(model100, organization110);
        assertEquals(2, users.size());
        userManager.remove(model100, organization110, userIdOptional.get());
        users = userManager.getAll(model100, organization110);
        assertEquals(1, users.size());
    }

    @Test
    @Order(5)
    public void readModelTest() {
        ModelManager modelManager = services.getModelManager();
        Optional<Model> model = modelManager.get(model100);
        assertTrue(model.isPresent());
    }

    @Test
    @Order(6)
    public void readAddressTest() {
        AddressManager addressManager = services.getAddressManager();
        Optional<Address> address = addressManager.get(model100, address110);
        assertTrue(address.isPresent());
    }

    @Test
    @Order(7)
    public void readOrganizationTest() {
        OrganizationManager organizationManager = services.getOrganizationManager();
        Optional<Organization> organization = organizationManager.get(model100, organization110);
        assertTrue(organization.isPresent());
    }

    @Test
    @Order(8)
    public void readUserTest() {
        UserManager userManager = services.getUserManager();
        Optional<User> user = userManager.get(model100, organization110, user111);
        assertTrue(user.isPresent());
    }

    @Test
    @Order(9)
    public void deleteModelTest() {
        ModelManager modelManager = services.getModelManager();
        modelManager.remove(model100);
    }

    @Test
    @Order(10)
    public void readModelAfterModelDeleteTest() {
        ModelManager modelManager = services.getModelManager();
        Optional<Model> model = modelManager.get(model100);
        assertTrue(model.isEmpty());
    }

    @Test
    @Order(11)
    public void readAddressAfterModelDeleteTest() {
        AddressManager addressManager = services.getAddressManager();
        Optional<Address> address = addressManager.get(model100, address110);
        assertTrue(address.isEmpty());
    }

    @Test
    @Order(12)
    public void readOrganizationAfterModelDeleteTest() {
        OrganizationManager organizationManager = services.getOrganizationManager();
        Optional<Organization> organization = organizationManager.get(model100, organization110);
        assertTrue(organization.isEmpty());
    }

    @Test
    @Order(13)
    public void readUserAfterModelDeleteTest() {
        UserManager userManager = services.getUserManager();
        Optional<User> user = userManager.get(model100, organization110, user111);
        assertTrue(user.isEmpty());
    }

}
