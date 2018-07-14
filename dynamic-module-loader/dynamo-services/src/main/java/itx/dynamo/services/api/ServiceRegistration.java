package itx.dynamo.services.api;

public interface ServiceRegistration<T> {

    String getModuleName();

    Class<T> getType();

    void registerLifeCycleListener(LifeCycleListener lifeCycleListener);

    T getInstance();

}
