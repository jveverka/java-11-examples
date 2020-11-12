package itx.examples.mongodb.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {

    private final String _id;
    private final String street;
    private final String city;

    @JsonCreator
    public Address(@JsonProperty("_id") String _id,
                   @JsonProperty("street") String street,
                   @JsonProperty("city") String city) {
        this._id = _id;
        this.street = street;
        this.city = city;
    }

    public String get_id() {
        return _id;
    }

    @JsonIgnore
    public String getId() {
        return _id;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

}
