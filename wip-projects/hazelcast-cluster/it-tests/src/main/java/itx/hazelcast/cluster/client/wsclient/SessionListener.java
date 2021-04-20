package itx.hazelcast.cluster.client.wsclient;

import itx.hazelcast.cluster.dto.MessageWrapper;

public interface SessionListener {

    void onSessionReady();

    void onTextMessage(String message);

    void onMessageWrapper(MessageWrapper messageWrapper);

    void onSessionClose();

}
