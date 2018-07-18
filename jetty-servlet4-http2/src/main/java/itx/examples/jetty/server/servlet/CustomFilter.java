package itx.examples.jetty.server.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

public class CustomFilter implements Filter {

    final private static Logger LOG = LoggerFactory.getLogger(CustomFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("init ...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOG.info("doFilter");
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
        LOG.info("destroy");
    }

}
