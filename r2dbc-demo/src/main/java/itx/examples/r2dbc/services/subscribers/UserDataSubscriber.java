package itx.examples.r2dbc.services.subscribers;

import itx.examples.r2dbc.dto.UserData;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class UserDataSubscriber implements Subscriber<UserData> {

    private static final Logger LOG = LoggerFactory.getLogger(UserDataSubscriber.class);

    private final CompletableFuture<UserData> future;

    public UserDataSubscriber() {
        this.future = new CompletableFuture<>();
    }

    @Override
    public void onSubscribe(Subscription s) {
        LOG.info("onSubscribe");
        s.request(1);
    }

    @Override
    public void onNext(UserData userData) {
        LOG.info("onNext");
        this.future.complete(userData);
    }

    @Override
    public void onError(Throwable t) {
        LOG.info("onError");
        this.future.completeExceptionally(t);
    }

    @Override
    public void onComplete() {
        LOG.info("onComplete");
        this.future.completeExceptionally(new UnsupportedOperationException());
    }

    public Future<UserData> getResult() {
        return future;
    }
}
