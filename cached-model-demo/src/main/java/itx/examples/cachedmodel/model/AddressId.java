package itx.examples.cachedmodel.model;

import itx.examples.cachedmodel.model.keys.ID;

public class AddressId extends ID {

    public AddressId(String id) {
        super(id);
    }

    public static AddressId from(String id) {
        return new AddressId(id);
    }

}
