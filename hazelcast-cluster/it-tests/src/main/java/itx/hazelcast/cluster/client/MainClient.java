package itx.hazelcast.cluster.client;

import itx.hazelcast.cluster.client.wsclient.SessionListener;
import itx.hazelcast.cluster.client.wsclient.WsClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MainClient {

    public static void main(String[] args) throws Exception {

        CountDownLatch cl = new CountDownLatch(1);
        SessionListener sessionListener = new SessionListener() {

            @Override
            public void onSessionReady() {
                cl.countDown();
            }

            @Override
            public void onTextMessage(String message) {

            }

            @Override
            public void onByteMessage(byte[] message) {

            }

            @Override
            public void onSessionClose() {

            }

        };

        WsClient clientApp = new WsClient("ws://localhost:8080/data/websocket", sessionListener);
        clientApp.start();
        cl.await(10, TimeUnit.SECONDS);
        clientApp.sendTextMessage("hello world");
        clientApp.sendByteMessage("hi there".getBytes());
        clientApp.stop();
    }

}
