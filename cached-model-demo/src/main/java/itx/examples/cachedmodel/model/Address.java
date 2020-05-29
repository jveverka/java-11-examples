package itx.examples.cachedmodel.model;

public class Address {

    private final AddressId id;
    private final String street;
    private final String city;

    public Address(AddressId id, String street, String city) {
        this.id = id;
        this.street = street;
        this.city = city;
    }

    public AddressId getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

}
