package itx.hazelcast.cluster.server.websocket;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class WebSocketCreatorImpl implements WebSocketCreator, WebSocketUnregisterService {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketCreatorImpl.class);

    private final Map<Long, WsEndpoint> wsEndPoints;
    private final AtomicLong atomicLong;

    public WebSocketCreatorImpl() {
        this.wsEndPoints = new ConcurrentHashMap<>();
        this.atomicLong = new AtomicLong(0);
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        LOG.info("createWebSocket ...");
        Long id = atomicLong.getAndIncrement();
        WsEndpoint wsEndpoint = new WsEndpoint(id, this);
        wsEndPoints.put(id, wsEndpoint);
        return wsEndpoint;
    }

    @Override
    public void unregister(Long id) {
        wsEndPoints.remove(id);
    }

}
