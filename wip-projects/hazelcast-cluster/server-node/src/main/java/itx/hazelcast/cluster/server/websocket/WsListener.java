package itx.hazelcast.cluster.server.websocket;

public interface WsListener {

    void onSessionStart(long sessionId);

    void onSessionTerminated(long sessionId);

    void onBinaryMessage(long sessionId, byte[] payload, int offset, int len);

    void onTextMessage(long sessionId, String message);

}
