package itx.rxjava.test.consumer;

import itx.rxjava.dto.DataItem;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;


public class SynchronousDataSubscriber extends AbstractObserver implements Subscriber<DataItem>  {

    private final List<DataItem> items;
    private final List<Throwable> errors;

    private Subscription s;

    public SynchronousDataSubscriber() {
        items = new ArrayList<>();
        errors = new ArrayList<>();
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
        getCl().countDown();
    }

    public void request(long n) {
        if (s != null) {
            s.request(n);
        }
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

}
