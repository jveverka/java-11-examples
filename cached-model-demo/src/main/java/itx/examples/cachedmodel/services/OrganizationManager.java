package itx.examples.cachedmodel.services;

import itx.examples.cachedmodel.model.AddressId;
import itx.examples.cachedmodel.model.ModelId;
import itx.examples.cachedmodel.model.Organization;
import itx.examples.cachedmodel.model.OrganizationId;

import java.util.Collection;
import java.util.Optional;

public interface OrganizationManager {

    Optional<OrganizationId> add(ModelId modelId, String name, AddressId addressId);

    Collection<Organization> getAll(ModelId modelId);

    Optional<Organization> get(ModelId modelId, OrganizationId id);

    void remove(ModelId modelId, OrganizationId id);

}
