package itx.hazelcast.cluster.server.websocket;

import java.io.IOException;

public interface WebSocketMessageDispatcher {

    void sendTextMessage(long sessionId, String message) throws IOException;

    void sendBinaryMessage(long sessionId, byte[] message) throws IOException;

}
