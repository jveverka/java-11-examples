package itx.dynamo.testmodules.service.executor.api;

import itx.dynamo.services.api.ModuleHandler;
import itx.dynamo.services.api.ServiceInjectionRequest;
import itx.dynamo.services.api.ServiceRegistration;

import java.util.Set;

public class ModuleHandlerImpl implements ModuleHandler {

    final private static System.Logger LOG =  System.getLogger(ModuleHandlerImpl.class.getName());

    public ModuleHandlerImpl() {
    }

    @Override
    public ServiceInjectionRequest requires() {
        return null;
    }

    @Override
    public Set<ServiceRegistration<?>> provides() {
        return null;
    }

    @Override
    public void start() {
        LOG.log(System.Logger.Level.INFO, "initializing");
    }

    @Override
    public void shutdown() {
        LOG.log(System.Logger.Level.INFO, "closing");
    }
}
