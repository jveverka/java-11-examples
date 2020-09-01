package itx.examples.r2dbc.services.subscribers;

import io.r2dbc.spi.Result;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class ResultSubscriber implements Subscriber<Result> {

    private static final Logger LOG = LoggerFactory.getLogger(ResultSubscriber.class);

    private final CompletableFuture<Result> future;

    public ResultSubscriber() {
        this.future = new CompletableFuture<>();
    }

    @Override
    public void onSubscribe(Subscription s) {
        LOG.info("onSubscribe");
        s.request(1);
    }

    @Override
    public void onNext(Result result) {
        future.complete(result);
    }

    @Override
    public void onError(Throwable t) {
        future.completeExceptionally(t);
    }

    @Override
    public void onComplete() {
        LOG.info("onComplete");
    }

    public Future<Result> getResult() {
        return future;
    }

}
