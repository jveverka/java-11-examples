package itx.rxjava.test.consumer;

import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import itx.rxjava.dto.DataItem;


public class SynchronousMaybeObserver extends AbstractObserver implements MaybeObserver<DataItem> {

    private boolean subscribed;
    private DataItem dataItem;
    private Throwable e;

    public SynchronousMaybeObserver() {
        this.subscribed = false;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.subscribed = true;
    }

    @Override
    public void onSuccess(DataItem dataItem) {
        this.dataItem = dataItem;
        this.getCl().countDown();
    }

    @Override
    public void onError(Throwable e) {
        this.e = e;
        this.getCl().countDown();
    }

    @Override
    public void onComplete() {
        this.getCl().countDown();
    }

    public boolean hasErrors() {
        return this.e != null;
    }

    public DataItem getDataItem() {
        return dataItem;
    }
}
