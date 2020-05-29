package itx.examples.cachedmodel.model;

import itx.examples.cachedmodel.model.keys.ID;

public class OrganizationId extends ID {

    public OrganizationId(String id) {
        super(id);
    }

    public static OrganizationId from(String id) {
        return new OrganizationId(id);
    }

}
