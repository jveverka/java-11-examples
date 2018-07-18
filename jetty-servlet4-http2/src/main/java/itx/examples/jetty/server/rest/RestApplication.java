package itx.examples.jetty.server.rest;

import itx.examples.jetty.common.services.SystemInfoService;
import org.glassfish.jersey.server.ResourceConfig;

public class RestApplication extends ResourceConfig {

    public RestApplication(SystemInfoService systemInfoService) {
        register(SystemInfoRest.class);
        register(RestRequestFilter.class);
        register(new ServiceBinder(systemInfoService));
    }

}
