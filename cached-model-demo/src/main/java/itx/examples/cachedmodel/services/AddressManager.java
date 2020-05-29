package itx.examples.cachedmodel.services;

import itx.examples.cachedmodel.model.Address;
import itx.examples.cachedmodel.model.AddressId;
import itx.examples.cachedmodel.model.ModelId;

import java.util.Collection;
import java.util.Optional;

public interface AddressManager {

    Optional<AddressId> add(ModelId modelId, String street, String city);

    Collection<Address> getAll(ModelId modelId);

    Optional<Address> get(ModelId modelId, AddressId id);

    void remove(ModelId modelId, AddressId id);

}
