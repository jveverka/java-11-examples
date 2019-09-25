package itx.rxjava.test.consumer;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import itx.rxjava.dto.DataItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SynchronousDataObserver implements Observer<DataItem> {

    private final List<DataItem> items;
    private final List<Throwable> errors;
    private final CountDownLatch cl;

    private Disposable d;

    public SynchronousDataObserver() {
        items = new ArrayList<>();
        errors = new ArrayList<>();
        cl = new CountDownLatch(1);
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
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

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return cl.await(timeout, unit);
    }

    public List<DataItem> getResults() {
        return items;
    }

    public List<Throwable> getErrors() {
        return errors;
    }

    public Disposable getDisposable() {
        return d;
    }

    public boolean isCompleted() {
        return cl.getCount() == 0;
    }

}
