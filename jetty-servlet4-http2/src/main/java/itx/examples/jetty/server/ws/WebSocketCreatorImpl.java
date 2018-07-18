package itx.examples.jetty.server.ws;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketCreatorImpl implements WebSocketCreator {

    final private static Logger LOG = LoggerFactory.getLogger(WebSocketCreatorImpl.class);

    public WebSocketCreatorImpl() {
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        LOG.info("createWebSocket ...");
        return new WsEndpoint();
    }

}
