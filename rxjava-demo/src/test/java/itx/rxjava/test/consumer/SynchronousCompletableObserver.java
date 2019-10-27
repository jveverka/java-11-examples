package itx.rxjava.test.consumer;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;


public class SynchronousCompletableObserver extends AbstractObserver implements CompletableObserver {

    private boolean subscribed;
    private Throwable e;

    public SynchronousCompletableObserver() {
        this.subscribed = false;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.subscribed = true;
    }

    @Override
    public void onComplete() {
        this.getCl().countDown();
    }

    @Override
    public void onError(Throwable e) {
        this.e = e;
        this.getCl().countDown();
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public boolean hasErrors() {
        return this.e != null;
    }

}
