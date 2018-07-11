package itx.dynamo.testmodules.service.compute.api;

import itx.dynamo.core.api.ModuleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuleHandlerImpl implements ModuleHandler {

    final private static Logger LOG = LoggerFactory.getLogger(ModuleHandlerImpl.class);

    @Override
    public void initialize() {
        LOG.info("initializing");

    }

    @Override
    public void close() throws Exception {
        LOG.info("closing");

    }

}
