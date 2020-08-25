package itx.examples.cachedmodel.services;

import itx.examples.cachedmodel.model.Address;
import itx.examples.cachedmodel.model.AddressId;
import itx.examples.cachedmodel.model.Model;
import itx.examples.cachedmodel.model.ModelId;
import itx.examples.cachedmodel.model.cache.Cache;
import itx.examples.cachedmodel.model.cache.OperationsBuilder;
import itx.examples.cachedmodel.model.keys.CK;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class AddressManagerImpl implements AddressManager {

    private final Cache cache;

    public AddressManagerImpl(Cache cache) {
        this.cache = cache;
    }

    @Override
    public Optional<AddressId> add(ModelId modelId, String street, String city) {
        synchronized (cache) {
            CK<Model> modelCk = CK.from(Model.class, modelId);
            Optional<Model> modelOptional = cache.get(modelCk);
            if (modelOptional.isPresent()) {
                AddressId id = AddressId.from(UUID.randomUUID().toString());
                Address address = new Address(id, street, city);
                CK ck = CK.from(Address.class, modelId, id);
                Model model = Model.addAddress(modelOptional.get(), id);
                Collection<OperationsBuilder.Operation> operations = OperationsBuilder.builder()
                        .addWriteOperation(ck, address)
                        .addWriteOperation(modelCk, model)
                        .build();
                cache.apply(operations);
                return Optional.of(id);
            }
            return Optional.empty();
        }
    }

    @Override
    public Collection<Address> getAll(ModelId modelId) {
        synchronized (cache) {
            CK<Model> modelCk = CK.from(Model.class, modelId);
            Optional<Model> modelOptional = cache.get(modelCk);
            Collection<Address> result = new ArrayList<>();
            if (modelOptional.isPresent()) {
                modelOptional.get().getAddresses().forEach(a -> {
                    CK<Address> ck = CK.from(Address.class, modelId, a);
                    Optional<Address> optional = cache.get(ck);
                    if (optional.isPresent()) {
                        result.add(optional.get());
                    }
                });
            }
            return result;
        }
    }

    @Override
    public Optional<Address> get(ModelId modelId, AddressId id) {
        synchronized (cache) {
            CK<Address> ck = CK.from(Address.class, modelId, id);
            return cache.get(ck);
        }
    }

    @Override
    public void remove(ModelId modelId, AddressId id) {
        synchronized (cache) {
            CK<Model> modelCk = CK.from(Model.class, modelId);
            Optional<Model> modelOptional = cache.get(modelCk);
            OperationsBuilder operationsBuilder = OperationsBuilder.builder();
            if (modelOptional.isPresent()) {
                Model model = Model.removeAddress(modelOptional.get(), id);
                operationsBuilder.addWriteOperation(modelCk, model);
            }
            CK ck = CK.from(Address.class, modelId, id);
            operationsBuilder.addDeleteOperation(ck);
            cache.apply(operationsBuilder.build());
        }
    }

}
