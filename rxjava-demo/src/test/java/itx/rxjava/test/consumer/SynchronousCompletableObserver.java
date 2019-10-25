package itx.rxjava.test.consumer;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SynchronousCompletableObserver implements CompletableObserver {

    private final CountDownLatch cl;

    private boolean subscribed;
    private Throwable e;

    public SynchronousCompletableObserver() {
        this.cl = new CountDownLatch(1);
        this.subscribed = false;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.subscribed = true;
    }

    @Override
    public void onComplete() {
        this.cl.countDown();
    }

    @Override
    public void onError(Throwable e) {
        this.e = e;
        this.cl.countDown();
    }

    public boolean isSubscribed() {
        return subscribed;
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

}
