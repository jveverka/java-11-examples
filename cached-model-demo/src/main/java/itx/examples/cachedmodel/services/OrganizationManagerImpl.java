package itx.examples.cachedmodel.services;

import itx.examples.cachedmodel.model.AddressId;
import itx.examples.cachedmodel.model.Model;
import itx.examples.cachedmodel.model.ModelId;
import itx.examples.cachedmodel.model.Organization;
import itx.examples.cachedmodel.model.OrganizationId;
import itx.examples.cachedmodel.model.cache.Cache;
import itx.examples.cachedmodel.model.cache.OperationsBuilder;
import itx.examples.cachedmodel.model.keys.CK;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class OrganizationManagerImpl implements OrganizationManager {

    private final Cache cache;

    public OrganizationManagerImpl(Cache cache) {
        this.cache = cache;
    }

    @Override
    public Optional<OrganizationId> add(ModelId modelId, String name, AddressId addressId) {
        synchronized (cache) {
            CK<Model> modelCk = CK.from(Model.class, modelId);
            Optional<Model> modelOptional = cache.get(modelCk);
            if (modelOptional.isPresent()) {
                OrganizationId organizationId = OrganizationId.from(UUID.randomUUID().toString());
                Organization organization = new Organization(organizationId, name, addressId, Collections.emptySet());
                CK ck = CK.from(Organization.class, modelId, organizationId);
                Model model = Model.addOrganization(modelOptional.get(), organizationId);
                Collection<OperationsBuilder.Operation> operations = OperationsBuilder.builder()
                        .addWriteOperation(ck, organization)
                        .addWriteOperation(modelCk, model)
                        .build();
                cache.apply(operations);
                return Optional.of(organizationId);
            }
            return Optional.empty();
        }
    }

    @Override
    public Collection<Organization> getAll(ModelId modelId) {
        synchronized (cache) {
            CK<Model> modelCk = CK.from(Model.class, modelId);
            Optional<Model> modelOptional = cache.get(modelCk);
            Collection<Organization> result = new ArrayList<>();
            if (modelOptional.isPresent()) {
                modelOptional.get().getOrganizations().forEach(o -> {
                    CK<Organization> ck = CK.from(Organization.class, modelId, o);
                    Optional<Organization> organizationOptional = cache.get(ck);
                    if (organizationOptional.isPresent()) {
                        result.add(organizationOptional.get());
                    }
                });
            }
            return result;
        }
    }

    @Override
    public Optional<Organization> get(ModelId modelId, OrganizationId id) {
        synchronized (cache) {
            CK<Organization> ck = CK.from(Organization.class, modelId, id);
            return cache.get(ck);
        }
    }

    @Override
    public void remove(ModelId modelId, OrganizationId id) {
        synchronized (cache) {
            CK<Model> modelCk = CK.from(Model.class, modelId);
            Optional<Model> modelOptional = cache.get(modelCk);
            OperationsBuilder operationsBuilder = OperationsBuilder.builder();
            if (modelOptional.isPresent()) {
                Model model = Model.removeOrganization(modelOptional.get(), id);
                operationsBuilder.addWriteOperation(modelCk, model);
            }
            CK ck = CK.from(Organization.class, modelId, id);
            operationsBuilder.addDeleteOperation(ck);
            cache.apply(operationsBuilder.build());
        }
    }

}
