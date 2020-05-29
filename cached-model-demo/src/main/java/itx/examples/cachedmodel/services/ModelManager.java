package itx.examples.cachedmodel.services;

import itx.examples.cachedmodel.model.Model;
import itx.examples.cachedmodel.model.ModelId;

import java.util.Collection;
import java.util.Optional;

public interface ModelManager {

    ModelId add(String name);

    Collection<Model> getAll();

    Optional<Model> get(ModelId id);

    void remove(ModelId id);

}
