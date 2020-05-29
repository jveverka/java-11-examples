package itx.examples.cachedmodel;

import itx.examples.cachedmodel.model.cache.Cache;
import itx.examples.cachedmodel.model.cache.CacheImpl;
import itx.examples.cachedmodel.persistence.PersistenceService;
import itx.examples.cachedmodel.persistence.PersistenceServiceAsynchronousImpl;
import itx.examples.cachedmodel.persistence.PersistenceServiceSynchronousImpl;
import itx.examples.cachedmodel.services.AddressManager;
import itx.examples.cachedmodel.services.AddressManagerImpl;
import itx.examples.cachedmodel.services.ModelManager;
import itx.examples.cachedmodel.services.ModelManagerImpl;
import itx.examples.cachedmodel.services.OrganizationManager;
import itx.examples.cachedmodel.services.OrganizationManagerImpl;
import itx.examples.cachedmodel.services.UserManager;
import itx.examples.cachedmodel.services.UserManagerImpl;

public final class ServiceBuilder {

    private Cache cache;
    private PersistenceService persistenceService;

    public ServiceBuilder withPersistence(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
        this.cache = new CacheImpl(persistenceService);
        return this;
    }

    public ServiceBuilder writeThroughCache() {
        this.persistenceService = new PersistenceServiceSynchronousImpl();
        this.cache = new CacheImpl(this.persistenceService);
        return this;
    }

    public ServiceBuilder writeBackCache() {
        this.persistenceService = new PersistenceServiceAsynchronousImpl();
        this.cache = new CacheImpl(this.persistenceService);
        return this;
    }

    public Services build() {
        if (this.cache == null) {
            this.persistenceService = new PersistenceServiceSynchronousImpl();
            this.cache = new CacheImpl(persistenceService);
        }
        return new Services(new AddressManagerImpl(cache), new ModelManagerImpl(cache),
                new OrganizationManagerImpl(cache), new UserManagerImpl(cache), persistenceService);
    }

    public static ServiceBuilder builder() {
        return new ServiceBuilder();
    }

    public class Services {

        private final AddressManager addressManager;
        private final ModelManager modelManager;
        private final OrganizationManager organizationManager;
        private final UserManager userManager;
        private final PersistenceService persistenceService;

        public Services(AddressManager addressManager, ModelManager modelManager,
                        OrganizationManager organizationManager, UserManager userManager,
                        PersistenceService persistenceService) {
            this.addressManager = addressManager;
            this.modelManager = modelManager;
            this.organizationManager = organizationManager;
            this.userManager = userManager;
            this.persistenceService = persistenceService;
        }

        public AddressManager getAddressManager() {
            return addressManager;
        }

        public ModelManager getModelManager() {
            return modelManager;
        }

        public OrganizationManager getOrganizationManager() {
            return organizationManager;
        }

        public UserManager getUserManager() {
            return userManager;
        }

        public PersistenceService getPersistenceService() {
            return persistenceService;
        }

    }

}
