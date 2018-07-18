package itx.examples.jetty.server.rest;

import itx.examples.jetty.common.dto.SystemInfo;
import itx.examples.jetty.common.services.SystemInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/system")
@Singleton
public class SystemInfoRest {

    private final static Logger LOG = LoggerFactory.getLogger(SystemInfoRest.class);

    @Context
    private HttpServletRequest request;

    @Inject
    private SystemInfoService systemInfoService;

    public SystemInfoRest() {
        LOG.info("init ...");
    }

    @Path("/info")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SystemInfo getSystemInfo() {
        LOG.info("rest: getSystemInfo {}", request.getSession().getId());
        return systemInfoService.getSystemInfo();
    }

}
