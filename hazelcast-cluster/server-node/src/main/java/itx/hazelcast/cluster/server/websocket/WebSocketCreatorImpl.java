package itx.hazelcast.cluster.server.websocket;

import itx.hazelcast.cluster.server.services.RequestRouter;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class WebSocketCreatorImpl implements WebSocketCreator, WebSocketMessageDispatcher, WebSocketUnregisterService {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketCreatorImpl.class);

    private final RequestRouter requestRouter;
    private final Map<Long, WsEndpoint> wsEndPoints;
    private final AtomicLong atomicLong;

    public WebSocketCreatorImpl(RequestRouter requestRouter) {
        this.wsEndPoints = new ConcurrentHashMap<>();
        this.atomicLong = new AtomicLong(0);
        this.requestRouter = requestRouter;
        this.requestRouter.setWebSocketMessageDispatcher(this);
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        LOG.info("createWebSocket ...");
        Long id = atomicLong.getAndIncrement();
        WsEndpoint wsEndpoint = new WsEndpoint(id, this, requestRouter);
        wsEndPoints.put(id, wsEndpoint);
        return wsEndpoint;
    }

    @Override
    public void unregister(Long id) {
        wsEndPoints.remove(id);
    }

    @Override
    public void sendTextMessage(long sessionId, String message) throws IOException {
        WsEndpoint wsEndpoint = this.wsEndPoints.get(sessionId);
        if (wsEndpoint != null) {
            wsEndpoint.sendTextMessage(message);
        }
    }

    @Override
    public void sendBinaryMessage(long sessionId, byte[] message) throws IOException {
        WsEndpoint wsEndpoint = this.wsEndPoints.get(sessionId);
        if (wsEndpoint != null) {
            wsEndpoint.sendBinaryMessage(message);
        }
    }

}
