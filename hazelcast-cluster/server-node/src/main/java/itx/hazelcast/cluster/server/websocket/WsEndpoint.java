package itx.hazelcast.cluster.server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

public class WsEndpoint implements WebSocketListener {

    final private static Logger LOG = LoggerFactory.getLogger(WsEndpoint.class);

    private Session session;
    private final Long id;
    private final WebSocketUnregisterService webSocketUnregisterService;
    private final WsListener listener;

    public WsEndpoint(Long id, WebSocketUnregisterService webSocketUnregisterService, WsListener listener) {
        LOG.info("init ...");
        this.id = id;
        this.webSocketUnregisterService = webSocketUnregisterService;
        this.listener = listener;
    }

    @Override
    public void onWebSocketConnect(Session session) {
        LOG.info("onWebSocketConnect: {}", id);
        this.session = session;
        this.listener.onSessionStart(id);
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        LOG.info("onWebSocketBinary: {} {} {}", id, offset, len);
        this.listener.onBinaryMessage(id, payload, offset, len);
    }

    @Override
    public void onWebSocketText(String message) {
        LOG.info("onWebSocketText: {} {}", id, message);
        this.listener.onTextMessage(id, message);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        LOG.info("onWebSocketClose: {} {} {} ", id, statusCode, reason);
        this.session = null;
        this.webSocketUnregisterService.unregister(id);
        this.listener.onSessionTerminated(id);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        LOG.error("onWebSocketError: {}", id, cause);
        this.session = null;
        this.webSocketUnregisterService.unregister(id);
        this.listener.onSessionTerminated(id);
    }

    public void sendTextMessage(String message) throws IOException {
        if (session != null) {
           session.getRemote().sendString(message);
        }
    }

    public void sendBinaryMessage(byte[] message) throws IOException {
        if (session != null) {
            session.getRemote().sendBytes(ByteBuffer.wrap(message));
        }
    }

    public Long getId() {
        return id;
    }

}
