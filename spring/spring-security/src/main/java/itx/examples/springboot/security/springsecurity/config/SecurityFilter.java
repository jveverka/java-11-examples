package itx.examples.springboot.security.springsecurity.config;

import itx.examples.springboot.security.springsecurity.services.UserAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);

    private final UserAccessService userAccessService;

    public SecurityFilter(UserAccessService userAccessService) {
        this.userAccessService = userAccessService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        String sessionId = httpServletRequest.getSession(true).getId();
        if (userAccessService.isAuthorized(sessionId)) {
            chain.doFilter(request, response);
        } else {
            LOG.error("session is not authorized: {}", sessionId);
            HttpServletResponse httpServletResponse = (HttpServletResponse)response;
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        }
    }

}
