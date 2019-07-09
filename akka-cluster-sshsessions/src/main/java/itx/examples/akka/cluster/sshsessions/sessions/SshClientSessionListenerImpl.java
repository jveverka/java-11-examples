package itx.examples.akka.cluster.sshsessions.sessions;

import itx.examples.akka.cluster.sshsessions.client.SshClientSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by juraj on 25.3.2017.
 */
public class SshClientSessionListenerImpl implements SshClientSessionListener {

    private static final Logger LOG = LoggerFactory.getLogger(SshClientSessionListenerImpl.class);

    private CountDownLatch countDownLatch;
    private String data;

    public SshClientSessionListenerImpl() {
        countDownLatch = new CountDownLatch(1);
    }

    public String awaitData(long time, TimeUnit timeUnit) throws InterruptedException {
        countDownLatch = new CountDownLatch(1);
        countDownLatch.await(time, timeUnit);
        return data;
    }

    public String awaitData() {
        countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await(1, TimeUnit.SECONDS);
            return data;
        } catch (InterruptedException e) {
        }
        return "";
    }

    @Override
    public void onData(String data) {
        this.data = data;
        countDownLatch.countDown();
    }

    @Override
    public void onError(String reason) {
        LOG.error("onError: {}", reason);
    }

    @Override
    public void onClose() {
        LOG.info("onClose");
    }

}
