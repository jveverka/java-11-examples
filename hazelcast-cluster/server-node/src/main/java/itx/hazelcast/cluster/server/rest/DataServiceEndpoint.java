package itx.hazelcast.cluster.server.rest;

import itx.hazelcast.cluster.server.services.DataService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Path;

@Path("/system")
@Singleton
public class DataServiceEndpoint {

    @Inject
    private DataService dataService;

}
