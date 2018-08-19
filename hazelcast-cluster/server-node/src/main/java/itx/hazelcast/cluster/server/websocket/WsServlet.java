package itx.hazelcast.cluster.server.websocket;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class WsServlet extends WebSocketServlet {

    public WsServlet() {
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.setCreator(new WebSocketCreatorImpl());
    }

}
