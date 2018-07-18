package itx.examples.jetty.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import java.io.IOException;

public class RestRequestFilter implements ContainerRequestFilter {

    private final static Logger LOG = LoggerFactory.getLogger(RestRequestFilter.class);

    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOG.info("filtering {}", request.getSession().getId());
    }

}
