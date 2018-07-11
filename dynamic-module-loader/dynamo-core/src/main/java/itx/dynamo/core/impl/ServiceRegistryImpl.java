package itx.dynamo.core.impl;

import itx.dynamo.core.api.ServiceRegistration;
import itx.dynamo.core.api.ServiceRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistryImpl implements ServiceRegistry {

    private final Map<Class<?>, Object> services;

    public ServiceRegistryImpl() {
        this.services = new ConcurrentHashMap<>();
    }

    @Override
    public ServiceRegistration registerService(Class<?> type, Object service) {
        services.put(type, service);
        return new ServiceRegistrationImpl(this, type);
    }

    @Override
    public <T> T getService(Class<T> type) {
        return (T)services.get(type);
    }

    protected void unregister(Class<?> type) {
        services.remove(type);
    }

}
