package itx.examples.cachedmodel.tests;

import itx.examples.cachedmodel.model.Address;
import itx.examples.cachedmodel.model.AddressId;
import itx.examples.cachedmodel.model.Model;
import itx.examples.cachedmodel.model.ModelId;
import itx.examples.cachedmodel.model.Organization;
import itx.examples.cachedmodel.model.OrganizationId;
import itx.examples.cachedmodel.model.User;
import itx.examples.cachedmodel.model.UserId;
import itx.examples.cachedmodel.model.keys.CK;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CKTests {

    @Test
    public void startsWithTest() {
        CK<Model> modelCK = CK.from(Model.class, ModelId.from("model-001"));
        CK<Address> addressCK = CK.from(Address.class, ModelId.from("model-001"), AddressId.from("address-001"));
        CK<Organization> organizationCK = CK.from(Organization.class, ModelId.from("model-001"), OrganizationId.from("organization-001"));
        CK<User> userCK = CK.from(User.class, ModelId.from("model-001"), OrganizationId.from("organization-001"), UserId.from("user-001"));
        assertTrue(modelCK.startsWith(modelCK));
        assertTrue(addressCK.startsWith(modelCK));
        assertTrue(addressCK.startsWith(addressCK));
        assertFalse(modelCK.startsWith(addressCK));
        assertFalse(addressCK.startsWith(organizationCK));
        assertTrue(organizationCK.startsWith(modelCK));
        assertTrue(userCK.startsWith(modelCK));
        assertTrue(userCK.startsWith(organizationCK));
        assertTrue(userCK.startsWith(userCK));
    }

}
