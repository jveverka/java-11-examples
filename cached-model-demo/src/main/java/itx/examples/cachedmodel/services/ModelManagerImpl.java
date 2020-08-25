package itx.examples.cachedmodel.services;

import itx.examples.cachedmodel.model.Model;
import itx.examples.cachedmodel.model.ModelId;
import itx.examples.cachedmodel.model.cache.Cache;
import itx.examples.cachedmodel.model.keys.CK;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class ModelManagerImpl implements ModelManager {

    private final Cache cache;

    public ModelManagerImpl(Cache cache) {
        this.cache = cache;
    }

    @Override
    public ModelId add(String name) {
        synchronized (cache) {
            ModelId id = ModelId.from(UUID.randomUUID().toString());
            Model model = new Model(id, name, Collections.emptySet(), Collections.emptySet());
            CK<Model> ck = CK.from(Model.class, id);
            cache.put(ck, model);
            return id;
        }
    }

    @Override
    public Collection<Model> getAll() {
        synchronized (cache) {
            return cache.getAll(Model.class);
        }
    }

    @Override
    public Optional<Model> get(ModelId id) {
        synchronized (cache) {
            CK<Model> ck = CK.from(Model.class, id);
            return cache.get(ck);
        }
    }

    @Override
    public void remove(ModelId id) {
        synchronized (cache) {
            CK<Model> ck = CK.from(Model.class, id);
            cache.remove(ck);
        }
    }

}
