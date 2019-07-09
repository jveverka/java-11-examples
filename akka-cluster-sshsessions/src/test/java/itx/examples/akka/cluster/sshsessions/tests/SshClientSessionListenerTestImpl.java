package itx.examples.akka.cluster.sshsessions.tests;

import itx.examples.akka.cluster.sshsessions.client.SshClientSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by juraj on 3/19/17.
 */
public class SshClientSessionListenerTestImpl implements SshClientSessionListener {

    private static final Logger LOG = LoggerFactory.getLogger(SshClientSessionListenerTestImpl.class);

    private String data;
    private CountDownLatch countDownLatch;

    public SshClientSessionListenerTestImpl() {
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    public void onData(String data) {
        LOG.info("onData: " + data);
        this.data = data;
        this.countDownLatch.countDown();
    }

    @Override
    public void onError(String reason) {
        LOG.info("onError: " + reason);

    }

    @Override
    public void onClose() {
        LOG.info("onClose");

    }

    public void setDataWait() {
        data = null;
        countDownLatch = new CountDownLatch(1);
    }

    public String waitForData(long timeout, TimeUnit timeUnit) throws InterruptedException {
        countDownLatch.await(timeout, timeUnit);
        return this.data;
    }

}
