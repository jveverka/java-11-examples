package itx.hazelcast.cluster.client.wsclient;

public interface SessionListener {

    void onSessionReady();

    void onTextMessage(String message);

    void onByteMessage(byte[] message);

    void onSessionClose();

}
