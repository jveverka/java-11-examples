package itx.examples.jetty.server.ws;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WsEndpoint implements WebSocketListener {

    final private static Logger LOG = LoggerFactory.getLogger(WsEndpoint.class);

    private Session session;

    public WsEndpoint() {
        LOG.info("init ...");
    }

    @Override
    public void onWebSocketConnect(Session session) {
        LOG.info("onWebSocketConnect: {}", session);
        this.session = session;
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        LOG.info("onWebSocketBinary: {} {}", offset, len);
    }

    @Override
    public void onWebSocketText(String message) {
        LOG.info("onWebSocketText: {}", message);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        LOG.info("onWebSocketClose: [{}] ", reason);
        this.session = null;
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        LOG.error("onWebSocketError: ", cause);
        this.session = null;
    }

    public void sendMessage(String message) throws IOException {
        if (session != null) {
           session.getRemote().sendString(message);
        }
    }

}
