package itx.rxjava;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import itx.rxjava.dto.DataItem;
import itx.rxjava.dto.DataQuery;
import itx.rxjava.producer.FlowableDataProducer;
import itx.rxjava.producer.ObservableDataProducer;

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

}
