package itx.rxjava.test.consumer;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import itx.rxjava.dto.DataItem;

import java.util.ArrayList;
import java.util.List;


public class SynchronousDataObserver extends AbstractObserver implements Observer<DataItem> {

    private final List<DataItem> items;
    private final List<Throwable> errors;

    private Disposable d;

    public SynchronousDataObserver() {
        items = new ArrayList<>();
        errors = new ArrayList<>();
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
        getCl().countDown();
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

}
