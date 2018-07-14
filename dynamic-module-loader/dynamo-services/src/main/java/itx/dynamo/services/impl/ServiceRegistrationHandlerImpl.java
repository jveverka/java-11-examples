package itx.dynamo.services.impl;

import itx.dynamo.services.api.LifeCycleListener;
import itx.dynamo.services.api.LifeCycleStatus;
import itx.dynamo.services.api.ServiceRegistrationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceRegistrationHandlerImpl<T> implements ServiceRegistrationHandler<T> {

    private final String moduleName;
    private final Class<T> type;
    private T instance;
    private List<LifeCycleListener> listeners;

    public ServiceRegistrationHandlerImpl(String moduleName, Class<T> type) {
        this.moduleName = moduleName;
        this.type = type;
        this.listeners = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public void registerLifeCycleListener(LifeCycleListener lifeCycleListener) {
        listeners.add(lifeCycleListener);
    }

    @Override
    public T getInstance() {
        return instance;
    }

    @Override
    public void setInstance(T service) {
        this.instance = instance;
    }

    @Override
    public int publishLIfeCycleStatus(LifeCycleStatus status) {
        AtomicInteger ai = new AtomicInteger(0);
        listeners.forEach( l -> {
            l.onStatusChange(status);
            ai.incrementAndGet();
        });
        return ai.get();
    }

    public void unregisterListener(LifeCycleListener lifeCycleListener) {
        listeners.remove(lifeCycleListener);
    }

}
