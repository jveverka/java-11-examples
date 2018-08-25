package itx.hazelcast.cluster.server.websocket;

import itx.hazelcast.cluster.server.services.RequestRouter;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class WsServlet extends WebSocketServlet {

    private final RequestRouter requestRouter;

    public WsServlet(RequestRouter requestRouter) {
        this.requestRouter = requestRouter;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.setCreator(new WebSocketCreatorImpl(requestRouter));
    }

}
