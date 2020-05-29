package itx.examples.cachedmodel.model;

import itx.examples.cachedmodel.model.keys.ID;

public class ModelId extends ID {

    public ModelId(String id) {
        super(id);
    }

    public static ModelId from(String id) {
        return new ModelId(id);
    }

}
