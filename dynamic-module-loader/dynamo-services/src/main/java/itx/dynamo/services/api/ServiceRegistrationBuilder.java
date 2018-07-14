package itx.dynamo.services.api;

import itx.dynamo.services.impl.ServiceRegistrationHandlerImpl;

public class ServiceRegistrationBuilder<T> {

    private String moduleName;
    private Class<T> type;
    private T instance;

    public ServiceRegistrationBuilder<T> setModuleName(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public ServiceRegistrationBuilder<T> setType(Class<T> type) {
        this.type = type;
        return this;
    }

    public ServiceRegistrationBuilder<T> setInstance(T instance) {
        this.instance = instance;
        return this;
    }

    public ServiceRegistrationHandler<T> build() {
        ServiceRegistrationHandler serviceRegistrationHandler = new ServiceRegistrationHandlerImpl<T>(moduleName, type);
        serviceRegistrationHandler.setInstance(instance);
        return serviceRegistrationHandler;
    }

}
