module itx.dynamo.testmodules.service.compute {
    exports itx.dynamo.testmodules.service.compute.api;
    provides itx.dynamo.services.api.ModuleHandler with itx.dynamo.testmodules.service.compute.api.ModuleHandlerImpl;
    requires itx.dynamo.services;
}
