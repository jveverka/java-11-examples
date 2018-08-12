package itx.examples.sshd.tests;

import itx.ssh.client.SshSessionListener;

import java.util.concurrent.CountDownLatch;

public class SshSessionListenerImpl implements SshSessionListener {

    private byte[] message;
    private CountDownLatch countDownLatch;

    @Override
    public void onMessage(byte[] message) {
        this.message = message;
        countDownLatch.countDown();
    }

    @Override
    public void onSessionClosed() {
        countDownLatch.countDown();
    }

    public void reset(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        this.message = null;
    }

    public byte[] getLastMessage() {
        return message;
    }


}
