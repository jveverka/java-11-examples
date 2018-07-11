package itx.dynamo.core.api;

import itx.dynamo.core.impl.ServiceRegistryImpl;

public interface ModuleManager extends AutoCloseable {

    void initialize();

    ServiceRegistryImpl getServiceRegistryImpl();

}
