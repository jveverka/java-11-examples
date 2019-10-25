package itx.rxjava;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import itx.rxjava.dto.DataItem;
import itx.rxjava.dto.DataQuery;
import itx.rxjava.dto.SingleDataQuery;
import itx.rxjava.producer.CompletableDataItem;
import itx.rxjava.producer.FlowableDataProducer;
import itx.rxjava.producer.MaybeOnSubscribeDataProducer;
import itx.rxjava.producer.ObservableDataProducer;
import itx.rxjava.producer.SingleOnSubscribeDataProducer;

import java.util.concurrent.Executor;

public class DataServiceImpl implements DataService {

    private final Executor executor;

    public DataServiceImpl(Executor executor) {
        this.executor = executor;
    }

    @Override
    public Flowable<DataItem> getDataFlowWithBackPressure(DataQuery dataQuery) {
        return Flowable.create(new FlowableDataProducer(executor, dataQuery), BackpressureStrategy.BUFFER);
    }

    @Override
    public Observable<DataItem> getDataFlow(DataQuery dataQuery) {
        return Observable.create(new ObservableDataProducer(executor, dataQuery));
    }

    @Override
    public Single<DataItem> getSingle(SingleDataQuery dataQuery) {
        return Single.create(new SingleOnSubscribeDataProducer(executor, dataQuery));
    }

    @Override
    public CompletableDataItem getCompletable(SingleDataQuery dataQuery) {
        return new CompletableDataItem(executor, dataQuery);
    }

    @Override
    public Maybe<DataItem> getMaybe(SingleDataQuery dataQuery) {
        return Maybe.create(new MaybeOnSubscribeDataProducer(executor, dataQuery));
    }

}
