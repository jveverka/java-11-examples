package itx.hazelcast.cluster.client.wsclient;

import itx.hazelcast.cluster.dto.MessageWrapper;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class SimpleWebSocket {

    final private static Logger LOG = LoggerFactory.getLogger(SimpleWebSocket.class);

    private final CountDownLatch openLatch;
    private final CountDownLatch closeLatch;
    private Session session;
    private SessionListener sessionListener;

    public SimpleWebSocket(SessionListener sessionListener) {
        this.openLatch = new CountDownLatch(1);
        this.closeLatch = new CountDownLatch(1);
        this.sessionListener = sessionListener;
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration,unit);
    }

    public boolean awaitOpen(int duration, TimeUnit unit) throws InterruptedException {
        return this.openLatch.await(duration,unit);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        LOG.info("Connection closed: {} - {}", statusCode, reason);
        this.session = null;
        this.closeLatch.countDown(); // trigger latch
        this.sessionListener.onSessionClose();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        LOG.info("onConnect");
        this.session = session;
        this.openLatch.countDown();
        this.sessionListener.onSessionReady();
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        LOG.info("Got msg: {}",msg);
        this.sessionListener.onTextMessage(msg);
    }

    @OnWebSocketMessage
    public void onMessage(InputStream inputStream) {
        try {
            MessageWrapper messageWrapper = MessageWrapper.parseFrom(inputStream.readAllBytes());
            this.sessionListener.onMessageWrapper(messageWrapper);
        } catch (IOException e) {
            LOG.error("Error: ", e);
        }
    }

    public void close() {
        if (session != null) {
            this.session.close(StatusCode.NORMAL, "Closing");
        }
    }

    public void sendTextMessage(String message) throws IOException {
        session.getRemote().sendString(message);
    }

    public void sendByteMessage(byte[] message) throws IOException {
        session.getRemote().sendBytes(ByteBuffer.wrap(message));
    }

}
