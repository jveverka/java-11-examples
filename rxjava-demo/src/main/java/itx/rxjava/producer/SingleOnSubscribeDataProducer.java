package itx.rxjava.producer;

import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import itx.rxjava.dto.DataItem;
import itx.rxjava.dto.DataQuery;

import java.util.concurrent.Executor;

public class SingleOnSubscribeDataProducer implements SingleOnSubscribe<DataItem> {

    private final Executor executor;
    private final DataQuery dataQuery;

    public SingleOnSubscribeDataProducer(Executor executor, DataQuery dataQuery) {
        this.executor = executor;
        this.dataQuery = dataQuery;
    }

    @Override
    public void subscribe(SingleEmitter<DataItem> emitter) throws Throwable {

    }

}
