package itx.rxjava.producer;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import itx.rxjava.dto.SingleDataQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompletableDataItemTask implements Runnable, Disposable {

    private static final Logger LOG = LoggerFactory.getLogger(CompletableDataItemTask.class);

    private final SingleDataQuery dataQuery;
    private final CompletableObserver observer;

    public CompletableDataItemTask(SingleDataQuery dataQuery, CompletableObserver observer) {
        this.dataQuery = dataQuery;
        this.observer = observer;
    }

    @Override
    public void run() {
        LOG.info("CompletableDataItemTask: {}", dataQuery.getData());
        observer.onSubscribe(this);
        observer.onComplete();
        LOG.info("CompletableDataItemTask: done.");
    }

    @Override
    public void dispose() {
        LOG.info("dispose");
    }

    @Override
    public boolean isDisposed() {
        return false;
    }

}
