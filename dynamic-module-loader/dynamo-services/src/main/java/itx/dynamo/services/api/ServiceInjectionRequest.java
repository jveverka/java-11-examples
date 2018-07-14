package itx.dynamo.services.api;

import java.util.Set;

public interface ServiceInjectionRequest {

    Set<Class<?>> requires();

    void setServices(Set<ServiceRegistration<?>> services);

}
