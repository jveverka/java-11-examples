package itx.examples.cachedmodel.services;

import itx.examples.cachedmodel.model.AddressId;
import itx.examples.cachedmodel.model.ModelId;
import itx.examples.cachedmodel.model.Organization;
import itx.examples.cachedmodel.model.OrganizationId;
import itx.examples.cachedmodel.model.User;
import itx.examples.cachedmodel.model.UserId;
import itx.examples.cachedmodel.model.cache.Cache;
import itx.examples.cachedmodel.model.cache.OperationsBuilder;
import itx.examples.cachedmodel.model.keys.CK;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class UserManagerImpl implements UserManager {

    private final Cache cache;

    public UserManagerImpl(Cache cache) {
        this.cache = cache;
    }

    @Override
    public Optional<UserId> add(ModelId modelId, OrganizationId id, String name, AddressId addressId) {
        synchronized (cache) {
            CK<Organization> organizationCK = CK.from(Organization.class, modelId, id);
            Optional<Organization> organizationOptional = cache.get(organizationCK);
            if (organizationOptional.isPresent()) {
                UserId userId = UserId.from(UUID.randomUUID().toString());
                User user = new User(userId, name, addressId);
                Organization organization = Organization.addUser(organizationOptional.get(), userId);
                CK<User> ck = CK.from(User.class, modelId, id, userId);
                Collection<OperationsBuilder.Operation> operations = OperationsBuilder.builder()
                        .addWriteOperation(ck, user)
                        .addWriteOperation(organizationCK, organization)
                        .build();
                cache.apply(operations);
                return Optional.of(userId);
            }
            return Optional.empty();
        }
    }

    @Override
    public Collection<User> getAll(ModelId modelId, OrganizationId id) {
        synchronized (cache) {
            CK<Organization> organizationCK = CK.from(Organization.class, modelId, id);
            Optional<Organization> organizationOptional = cache.get(organizationCK);
            Collection<User> result = new ArrayList<>();
            if (organizationOptional.isPresent()) {
                organizationOptional.get().getUsers().forEach(u -> {
                    CK<User> ck = CK.from(User.class, modelId, id, u);
                    Optional<User> userOptional = cache.get(ck);
                    if (userOptional.isPresent()) {
                        result.add(userOptional.get());
                    }
                });
            }
            return result;
        }
    }

    @Override
    public Optional<User> get(ModelId modelId, OrganizationId id, UserId userId) {
        synchronized (cache) {
            CK<User> ck = CK.from(User.class, modelId, id, userId);
            return cache.get(ck);
        }
    }

    @Override
    public void remove(ModelId modelId, OrganizationId id, UserId userId) {
        synchronized (cache) {
            CK<Organization> organizationCK = CK.from(Organization.class, modelId, id);
            Optional<Organization> organizationOptional = cache.get(organizationCK);
            OperationsBuilder operationsBuilder = OperationsBuilder.builder();
            if (organizationOptional.isPresent()) {
                Organization organization = Organization.removeUser(organizationOptional.get(), userId);
                operationsBuilder.addWriteOperation(organizationCK, organization);
            }
            CK<User> userCK = CK.from(User.class, modelId, id, userId);
            operationsBuilder.addDeleteOperation(userCK);
            cache.apply(operationsBuilder.build());
        }
    }

}
