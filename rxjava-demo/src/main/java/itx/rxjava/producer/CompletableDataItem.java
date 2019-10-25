package itx.rxjava.producer;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import itx.rxjava.dto.SingleDataQuery;

import java.util.concurrent.Executor;

public class CompletableDataItem extends Completable {

    private final SingleDataQuery dataQuery;
    private final Executor executor;

    public CompletableDataItem(Executor executor, SingleDataQuery dataQuery) {
        this.executor = executor;
        this.dataQuery = dataQuery;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
        executor.execute(new CompletableDataItemTask(dataQuery, observer));
    }

}
