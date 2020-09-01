package itx.examples.r2dbc.services.subscribers;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class RowsUpdatedSubscriber implements Subscriber<Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(RowsUpdatedSubscriber.class);

    private final CompletableFuture<Integer> future;

    public RowsUpdatedSubscriber() {
        this.future = new CompletableFuture<>();
    }

    @Override
    public void onSubscribe(Subscription s) {
        LOG.info("onSubscribe");
        s.request(1);
    }

    @Override
    public void onNext(Integer integer) {
        this.future.complete(integer);
    }

    @Override
    public void onError(Throwable t) {
        this.future.completeExceptionally(t);
    }

    @Override
    public void onComplete() {
        LOG.info("onComplete");
    }

    public Future<Integer> getResult() {
        return future;
    }
}
