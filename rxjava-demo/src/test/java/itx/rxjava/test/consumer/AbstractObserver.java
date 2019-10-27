package itx.rxjava.test.consumer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public abstract class AbstractObserver {

    private final CountDownLatch cl;

    protected AbstractObserver() {
        this.cl = new CountDownLatch(1);
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return this.cl.await(timeout, unit);
    }

    public boolean isCompleted() {
        return cl.getCount() == 0;
    }

    protected CountDownLatch getCl() {
        return cl;
    }

}
