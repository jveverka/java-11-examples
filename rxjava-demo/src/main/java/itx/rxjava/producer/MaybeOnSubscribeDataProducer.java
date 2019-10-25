package itx.rxjava.producer;

import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.core.MaybeOnSubscribe;
import itx.rxjava.dto.DataItem;
import itx.rxjava.dto.SingleDataQuery;

import java.util.concurrent.Executor;

public class MaybeOnSubscribeDataProducer implements MaybeOnSubscribe<DataItem> {

    private final Executor executor;
    private final SingleDataQuery dataQuery;

    public MaybeOnSubscribeDataProducer(Executor executor, SingleDataQuery dataQuery) {
        this.executor = executor;
        this.dataQuery = dataQuery;
    }

    @Override
    public void subscribe(MaybeEmitter<DataItem> emitter) throws Throwable {
        this.executor.execute(new MaybeEmitterTask(emitter, dataQuery));
    }

}
