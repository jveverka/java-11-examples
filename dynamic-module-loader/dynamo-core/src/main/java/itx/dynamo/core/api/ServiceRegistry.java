package itx.dynamo.core.api;

public interface ServiceRegistry {

    ServiceRegistration registerService(Class<?> type, Object service);

    <T> T getService(Class<T> type);

}
