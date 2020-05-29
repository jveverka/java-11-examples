package itx.examples.cachedmodel.services;

import itx.examples.cachedmodel.model.AddressId;
import itx.examples.cachedmodel.model.ModelId;
import itx.examples.cachedmodel.model.OrganizationId;
import itx.examples.cachedmodel.model.User;
import itx.examples.cachedmodel.model.UserId;

import java.util.Collection;
import java.util.Optional;

public interface UserManager {

    Optional<UserId> add(ModelId modelId, OrganizationId id, String name, AddressId addressId);

    Collection<User> getAll(ModelId modelId, OrganizationId id);

    Optional<User> get(ModelId modelId, OrganizationId id, UserId userId);

    void remove(ModelId modelId, OrganizationId id, UserId userId);

}
