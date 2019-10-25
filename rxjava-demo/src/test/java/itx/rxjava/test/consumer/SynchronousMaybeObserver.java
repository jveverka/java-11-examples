package itx.rxjava.test.consumer;

import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import itx.rxjava.dto.DataItem;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class SynchronousMaybeObserver implements MaybeObserver<DataItem> {

    private final CountDownLatch cl;

    private boolean subscribed;
    private DataItem dataItem;
    private Throwable e;

    public SynchronousMaybeObserver() {
        this.cl = new CountDownLatch(1);
        this.subscribed = false;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.subscribed = true;
    }

    @Override
    public void onSuccess(DataItem dataItem) {
        this.dataItem = dataItem;
        cl.countDown();
    }

    @Override
    public void onError(Throwable e) {
        this.e = e;
        cl.countDown();
    }

    @Override
    public void onComplete() {
        cl.countDown();
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return this.cl.await(timeout, unit);
    }

    public boolean isCompleted() {
        return cl.getCount() == 0;
    }

    public boolean hasErrors() {
        return this.e != null;
    }

    public DataItem getDataItem() {
        return dataItem;
    }
}
