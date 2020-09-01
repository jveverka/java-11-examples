package itx.examples.r2dbc.services.subscribers;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class VoidSubscriber implements Subscriber<Void> {

    private static final Logger LOG = LoggerFactory.getLogger(VoidSubscriber.class);

    private final CountDownLatch cl;

    public VoidSubscriber() {
        this.cl = new CountDownLatch(1);
    }

    @Override
    public void onSubscribe(Subscription s) {
        LOG.info("onSubscribe");
        s.request(1);
    }

    @Override
    public void onNext(Void unused) {
        this.cl.countDown();
    }

    @Override
    public void onError(Throwable t) {
    }

    @Override
    public void onComplete() {
        LOG.info("onComplete");
        this.cl.countDown();
    }

    public void await() throws InterruptedException {
        cl.await();
    }

}
