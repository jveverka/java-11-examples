package itx.examples.cachedmodel.model;

import java.util.HashSet;
import java.util.Set;

public class Model {

    private final ModelId id;
    private final String name;
    private final Set<OrganizationId> organizations;
    private final Set<AddressId> addresses;

    public Model(ModelId id, String name, Set<OrganizationId> organizations, Set<AddressId> addresses) {
        this.id = id;
        this.name = name;
        this.organizations = organizations;
        this.addresses = addresses;
    }

    public ModelId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<OrganizationId> getOrganizations() {
        return organizations;
    }

    public Set<AddressId> getAddresses() {
        return addresses;
    }

    public static Model addOrganization(Model model, OrganizationId organizationId) {
        Set<OrganizationId> organizations = new HashSet<>();
        organizations.addAll(model.organizations);
        organizations.add(organizationId);
        return new Model(model.id, model.name, organizations, model.addresses);
    }

    public static Model removeOrganization(Model model, OrganizationId organizationId) {
        Set<OrganizationId> organizations = new HashSet<>();
        organizations.addAll(model.organizations);
        organizations.remove(organizationId);
        return new Model(model.id, model.name, organizations, model.addresses);
    }

    public static Model addAddress(Model model, AddressId addressId) {
        Set<AddressId> addresses = new HashSet<>();
        addresses.addAll(model.addresses);
        addresses.add(addressId);
        return new Model(model.id, model.name, model.organizations, addresses);
    }

    public static Model removeAddress(Model model, AddressId addressId) {
        Set<AddressId> addresses = new HashSet<>();
        addresses.addAll(model.addresses);
        addresses.remove(addressId);
        return new Model(model.id, model.name, model.organizations, addresses);
    }

}
