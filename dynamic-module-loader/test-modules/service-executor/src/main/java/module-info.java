module itx.dynamo.testmodules.service.executor {
    exports itx.dynamo.testmodules.service.executor.api;
    provides itx.dynamo.services.api.ModuleHandler with itx.dynamo.testmodules.service.executor.api.ModuleHandlerImpl;
    requires itx.dynamo.services;
}
