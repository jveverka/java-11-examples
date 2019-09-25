package itx.rxjava.test.consumer;

import itx.rxjava.dto.DataItem;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SynchronousDataSubscriber implements Subscriber<DataItem>  {

    private final List<DataItem> items;
    private final List<Throwable> errors;
    private final CountDownLatch cl;

    private Subscription s;

    public SynchronousDataSubscriber() {
        items = new ArrayList<>();
        errors = new ArrayList<>();
        cl = new CountDownLatch(1);
    }

    @Override
    public void onSubscribe(Subscription s) {
        this.s = s;
    }

    @Override
    public void onNext(DataItem dataItem) {
        items.add(dataItem);
    }

    @Override
    public void onError(Throwable t) {
        errors.add(t);
    }

    @Override
    public void onComplete() {
        cl.countDown();
    }

    public void request(long n) {
        if (s != null) {
            s.request(n);
        }
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return cl.await(timeout, unit);
    }

    public List<DataItem> getResults() {
        return items;
    }

    public List<Throwable> getErrors() {
        return errors;
    }

    public Subscription getSubscription() {
        return s;
    }

    public boolean isCompleted() {
        return cl.getCount() == 0;
    }
}
