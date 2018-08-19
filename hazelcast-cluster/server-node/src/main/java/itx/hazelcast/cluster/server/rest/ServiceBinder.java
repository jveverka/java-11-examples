package itx.hazelcast.cluster.server.rest;

import itx.hazelcast.cluster.server.services.DataService;
import org.glassfish.jersey.internal.inject.AbstractBinder;

public class ServiceBinder extends AbstractBinder {

    private DataService dataService;

    public ServiceBinder(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    protected void configure() {
        bind(dataService).to(DataService.class);
    }

}
