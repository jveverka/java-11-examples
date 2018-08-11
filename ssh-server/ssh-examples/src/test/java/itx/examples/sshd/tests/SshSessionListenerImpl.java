package itx.examples.sshd.tests;

import itx.ssh.client.SshSessionListener;

import java.util.concurrent.CountDownLatch;

public class SshSessionListenerImpl implements SshSessionListener {

    private byte[] serverEvent;
    private CountDownLatch countDownLatch;

    @Override
    public void onServerEvent(byte[] serverEvent) {
        this.serverEvent = serverEvent;
        countDownLatch.countDown();
    }

    @Override
    public void onSessionClosed() {
        countDownLatch.countDown();
    }

    public void reset(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        this.serverEvent = null;
    }

    public byte[] getLastMessage() {
        return serverEvent;
    }


}
