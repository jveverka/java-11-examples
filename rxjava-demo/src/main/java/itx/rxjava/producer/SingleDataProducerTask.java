package itx.rxjava.producer;

import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Cancellable;
import itx.rxjava.dto.DataItem;
import itx.rxjava.dto.SingleDataQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleDataProducerTask implements Runnable, Cancellable, Disposable {

    private static final Logger LOG = LoggerFactory.getLogger(SingleDataProducerTask.class);

    private final SingleEmitter<DataItem> emitter;
    private final SingleDataQuery dataQuery;

    private boolean disposed;

    public SingleDataProducerTask(SingleEmitter<DataItem> emitter, SingleDataQuery dataQuery) {
        this.emitter = emitter;
        this.dataQuery = dataQuery;
        this.disposed = false;
    }

    @Override
    public void dispose() {
        disposed = true;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    @Override
    public void cancel() throws Throwable {
        LOG.info("cancel");
    }

    @Override
    public void run() {
        LOG.info("SingleDataProducerTask: {}", dataQuery.getData());
        emitter.setCancellable(this);
        emitter.setDisposable(this);
        emitter.onSuccess(new DataItem(dataQuery.getData(), "single-data-result", 1));
        LOG.info("SingleDataProducerTask: done.");
    }

}
