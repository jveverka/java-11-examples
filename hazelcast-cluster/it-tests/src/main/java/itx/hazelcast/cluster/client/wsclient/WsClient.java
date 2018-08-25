package itx.hazelcast.cluster.client.wsclient;

import itx.hazelcast.cluster.dto.MessageWrapper;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

public class WsClient {

    private WebSocketClient client;
    private String destUri;
    private SessionListener sessionListener;
    private SimpleWebSocket socket;

    public WsClient(String destUri, SessionListener sessionListener) {
        this.destUri = destUri;
        this.sessionListener = sessionListener;
    }

    public void start() throws Exception {
        client = new WebSocketClient();
        client.start();
        URI echoUri = new URI(destUri);
        ClientUpgradeRequest request = new ClientUpgradeRequest();
        socket = new SimpleWebSocket(sessionListener);
        client.connect(socket, echoUri, request);
        socket.awaitOpen(10, TimeUnit.SECONDS);
    }

    public void sendTextMessage(String message) throws IOException {
        socket.sendTextMessage(message);
    }

    public void sendMessageWrapper(MessageWrapper messageWrapper) throws IOException {
        socket.sendByteMessage(messageWrapper.toByteArray());
    }

    public void stop() throws Exception {
        socket.close();
        socket.awaitClose(10, TimeUnit.SECONDS);
        client.stop();
    }

}
