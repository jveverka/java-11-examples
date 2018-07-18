package itx.examples.jetty.server.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class CustomHttpSessionListener implements HttpSessionListener {

    final private static Logger LOG = LoggerFactory.getLogger(CustomHttpSessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        LOG.info("sessionCreated: {}", httpSessionEvent.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        LOG.info("sessionDestroyed: {}", httpSessionEvent.getSession().getId());
    }

}
