package itx.examples.sshd.tests;

import itx.ssh.client.SshSessionListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SshSessionListenerImpl implements SshSessionListener {

    private byte[] message;
    private CountDownLatch countDownLatch;
    private List<byte[]> messages;

    public SshSessionListenerImpl() {
        this.messages = new ArrayList<>();
    }

    @Override
    public void onMessage(byte[] message) {
        this.message = message;
        this.messages.add(message);
        this.countDownLatch.countDown();
    }

    @Override
    public void onSessionClosed() {
        countDownLatch.countDown();
    }

    public void reset(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        this.message = null;
        this.messages.clear();
    }

    public byte[] getLastMessage() {
        return message;
    }

    public List<byte[]> getMessages() {
        return Collections.unmodifiableList(messages);
    }

}
