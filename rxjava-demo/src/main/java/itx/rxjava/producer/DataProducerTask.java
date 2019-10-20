package itx.rxjava.producer;

import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Cancellable;
import itx.rxjava.dto.DataItem;
import itx.rxjava.dto.DataQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataProducerTask implements Runnable, Cancellable, Disposable {

    private static final Logger LOG = LoggerFactory.getLogger(DataProducerTask.class);

    private final Emitter<DataItem> emitter;
    private final DataQuery query;

    private boolean disposed;

    public DataProducerTask(Emitter<DataItem> emitter, DataQuery query) {
        this.emitter = emitter;
        this.query = query;
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
        LOG.info("cancel noop");
    }

    @Override
    public void run() {
        LOG.info("DataProducerTask: {} {}", query.getData(), query.getResults());
        for (int i=0; i < query.getResults(); i++) {
            emitter.onNext(new DataItem(query.getData(), query.getData() + "-" + i, i));
        }
        emitter.onComplete();
        LOG.info("DataProducerTask: done.");
    }

}
