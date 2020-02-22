package itx.examples.springboot.security.springsecurity.jwt.config;

import itx.examples.springboot.security.springsecurity.jwt.services.JWTUtils;
import itx.examples.springboot.security.springsecurity.jwt.services.UserAccessService;
import itx.examples.springboot.security.springsecurity.jwt.services.dto.JWToken;
import itx.examples.springboot.security.springsecurity.jwt.services.dto.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class SecurityFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);

    private final UserAccessService userAccessService;

    public SecurityFilter(UserAccessService userAccessService) {
        this.userAccessService = userAccessService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        String authorization = httpServletRequest.getHeader("Authorization");
        JWToken jwToken = JWToken.from(JWTUtils.extractJwtToken(authorization));
        Optional<UserData> userData = userAccessService.isAuthenticated(jwToken);
        if (userData.isPresent()) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(new AuthenticationImpl(userData.get().getUserId().getId(), userData.get().getRoles()));
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            chain.doFilter(request, response);
        } else {
            LOG.error("not authorized !");
            HttpServletResponse httpServletResponse = (HttpServletResponse)response;
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        }
    }

}
