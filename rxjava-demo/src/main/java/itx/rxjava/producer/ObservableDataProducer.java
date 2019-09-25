package itx.rxjava.producer;

import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import itx.rxjava.dto.DataItem;
import itx.rxjava.dto.DataQuery;

import java.util.concurrent.Executor;

public class ObservableDataProducer implements ObservableOnSubscribe<DataItem> {

    private final Executor executor;
    private final DataQuery query;

    public ObservableDataProducer(Executor executor, DataQuery query) {
        this.executor = executor;
        this.query = query;
    }

    @Override
    public void subscribe(ObservableEmitter<DataItem> emitter) throws Throwable {
        DataProducerTask dirScannerTask = new DataProducerTask(emitter, query);
        executor.execute(dirScannerTask);
    }

}
