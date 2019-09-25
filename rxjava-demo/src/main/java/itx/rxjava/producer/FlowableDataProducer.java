package itx.rxjava.producer;

import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import itx.rxjava.dto.DataItem;
import itx.rxjava.dto.DataQuery;

import java.util.concurrent.Executor;

public class FlowableDataProducer implements FlowableOnSubscribe<DataItem> {

    private final Executor executor;
    private final DataQuery query;

    public FlowableDataProducer(Executor executor, DataQuery query) {
        this.executor = executor;
        this.query = query;
    }

    @Override
    public void subscribe(FlowableEmitter<DataItem> emitter) throws Throwable {
        DataProducerTask dirScannerTask = new DataProducerTask(emitter, query);
        executor.execute(dirScannerTask);
    }

}
