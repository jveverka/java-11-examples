package itx.hazelcast.cluster.client.tests;

import itx.hazelcast.cluster.client.wsclient.SessionListener;
import itx.hazelcast.cluster.dto.MessageWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BlockingSessionListener implements SessionListener {

    private final CountDownLatch clStart;
    private CountDownLatch clMessageWrapper;
    private final List<MessageWrapper> messageBuffer;

    public BlockingSessionListener() {
        this.clStart = new CountDownLatch(1);
        this.messageBuffer = new ArrayList<>();
    }

    @Override
    public void onSessionReady() {
        clStart.countDown();
    }

    public void awaitForSessionReady(long time, TimeUnit timeUnit) throws InterruptedException {
        clStart.await(time, timeUnit);
    }

    public void resetMessageBuffer(int expectedMessageCount) {
        clMessageWrapper = new CountDownLatch(expectedMessageCount);
        messageBuffer.clear();
    }

    public List<MessageWrapper> awaitMessages(long time, TimeUnit timeUnit) throws InterruptedException {
        clMessageWrapper.await(time, timeUnit);
        return Collections.unmodifiableList(messageBuffer);
    }

    @Override
    public void onTextMessage(String message) {
    }

    @Override
    public void onMessageWrapper(MessageWrapper messageWrapper) {
        messageBuffer.add(messageWrapper);
        clMessageWrapper.countDown();
    }

    @Override
    public void onSessionClose() {

    }

}
