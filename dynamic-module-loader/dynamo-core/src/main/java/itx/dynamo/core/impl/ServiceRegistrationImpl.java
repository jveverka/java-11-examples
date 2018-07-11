package itx.dynamo.core.impl;

import itx.dynamo.core.api.ServiceRegistration;

public class ServiceRegistrationImpl implements ServiceRegistration {

    private final ServiceRegistryImpl serviceRegistry;
    private final Class<?> type;

    protected ServiceRegistrationImpl(ServiceRegistryImpl serviceRegistry, Class<?> type) {
        this.serviceRegistry = serviceRegistry;
        this.type = type;
    }

    @Override
    public void close() throws Exception {
        serviceRegistry.unregister(type);
    }

}
