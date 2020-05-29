package itx.examples.cachedmodel.model;

import java.util.HashSet;
import java.util.Set;

public class Organization {

    private final OrganizationId id;
    private final String name;
    private final AddressId addressId;
    private final Set<UserId> users;

    public Organization(OrganizationId id, String name, AddressId addressId, Set<UserId> users) {
        this.id = id;
        this.name = name;
        this.addressId = addressId;
        this.users = users;
    }

    public OrganizationId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AddressId getAddressId() {
        return addressId;
    }

    public Set<UserId> getUsers() {
        return users;
    }

    public static Organization addUser(Organization organization, UserId userId) {
        Set<UserId> users = new HashSet<>();
        users.addAll(organization.users);
        users.add(userId);
        return new Organization(organization.id, organization.name, organization.addressId, users);
    }

    public static Organization removeUser(Organization organization, UserId userId) {
        Set<UserId> users = new HashSet<>();
        users.addAll(organization.users);
        users.remove(userId);
        return new Organization(organization.id, organization.name, organization.addressId, users);
    }

}
