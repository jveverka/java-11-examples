package itx.examples.cachedmodel.model;

import itx.examples.cachedmodel.model.keys.ID;

public class UserId extends ID {

    public UserId(String id) {
        super(id);
    }

    public static UserId from(String id) {
        return new UserId(id);
    }

}
