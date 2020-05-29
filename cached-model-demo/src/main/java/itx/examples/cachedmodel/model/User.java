package itx.examples.cachedmodel.model;

public class User {

    private final UserId id;
    private final String name;
    private final AddressId address;

    public User(UserId id, String name, AddressId address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public UserId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AddressId getAddress() {
        return address;
    }

}
