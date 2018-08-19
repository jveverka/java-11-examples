package itx.hazelcast.cluster.server.rest;

import itx.hazelcast.cluster.server.services.DataService;
import org.glassfish.jersey.server.ResourceConfig;

public class RestApplication extends ResourceConfig {

    public RestApplication(DataService dataService) {
        register(DataServiceEndpoint.class);
        register(new ServiceBinder(dataService));
    }

}
