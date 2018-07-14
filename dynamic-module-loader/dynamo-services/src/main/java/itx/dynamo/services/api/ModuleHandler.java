package itx.dynamo.services.api;

import java.util.Set;

public interface ModuleHandler {

    ServiceInjectionRequest requires();

    Set<ServiceRegistration<?>> provides();

    void start();

    void shutdown();

}
