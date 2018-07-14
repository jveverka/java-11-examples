package itx.dynamo.services.api;

public interface ServiceRegistrationHandler<T> extends ServiceRegistration<T> {

    void setInstance(T service);

    int publishLIfeCycleStatus(LifeCycleStatus status);

    default ServiceRegistration getServiceRegistration() {
        return this;
    }

}
