package itx.rxjava.producer;

import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import itx.rxjava.dto.DataItem;
import itx.rxjava.dto.SingleDataQuery;

import java.util.concurrent.Executor;

public class SingleOnSubscribeDataProducer implements SingleOnSubscribe<DataItem> {

    private final Executor executor;
    private final SingleDataQuery dataQuery;

    public SingleOnSubscribeDataProducer(Executor executor, SingleDataQuery dataQuery) {
        this.executor = executor;
        this.dataQuery = dataQuery;
    }

    @Override
    public void subscribe(SingleEmitter<DataItem> emitter) throws Throwable {
        this.executor.execute(new SingleDataProducerTask(emitter, dataQuery));
    }

}
