package itx.rxjava.producer;

import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Cancellable;
import itx.rxjava.dto.DataItem;
import itx.rxjava.dto.SingleDataQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaybeEmitterTask implements Runnable, Cancellable, Disposable {

    private static final Logger LOG = LoggerFactory.getLogger(MaybeEmitterTask.class);

    private final MaybeEmitter<DataItem> emitter;
    private final SingleDataQuery dataQuery;

    boolean disposed;

    public MaybeEmitterTask(MaybeEmitter<DataItem> emitter, SingleDataQuery dataQuery) {
        this.emitter = emitter;
        this.dataQuery = dataQuery;
        this.disposed = false;
    }

    @Override
    public void run() {
        LOG.info("MaybeEmitterTask: {}", dataQuery.getData());
        emitter.setCancellable(this);
        emitter.setDisposable(this);
        emitter.onSuccess(new DataItem(dataQuery.getData(), "maybe-data-result", 1));
        emitter.onComplete();
        LOG.info("MaybeEmitterTask: done.");
    }

    @Override
    public void dispose() {
        LOG.info("dispose");
        this.disposed = true;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    @Override
    public void cancel() throws Throwable {
        LOG.info("cancel");
    }

}
