package itx.examples.jetty.server.rest;

import itx.examples.jetty.common.services.SystemInfoService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ServiceBinder extends AbstractBinder {

    private SystemInfoService systemInfoService;

    public ServiceBinder(SystemInfoService systemInfoService) {
        this.systemInfoService = systemInfoService;
    }

    @Override
    protected void configure() {
        bind(systemInfoService).to(SystemInfoService.class);
    }

}
