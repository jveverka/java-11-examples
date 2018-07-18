package itx.examples.jetty.server.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;

public class CustomHttpSessionIdListener implements HttpSessionIdListener {

    final private static Logger LOG = LoggerFactory.getLogger(CustomHttpSessionIdListener.class);

    @Override
    public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
        LOG.info("sessionIdChanged: {}->{}", oldSessionId, event.getSession().getId());
    }

}
