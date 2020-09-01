package itx.examples.r2dbc.services.subscribers;

import io.r2dbc.spi.Connection;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class ConnectionSubscriber implements Subscriber<Connection> {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionSubscriber.class);

    private final CompletableFuture<Connection> future;

    public ConnectionSubscriber() {
        future = new CompletableFuture<>();
    }

    @Override
    public void onSubscribe(Subscription s) {
        LOG.info("onSubscribe");
        s.request(1);
    }

    @Override
    public void onNext(Connection connection) {
        future.complete(connection);
    }

    @Override
    public void onError(Throwable t) {
        future.completeExceptionally(t);
    }

    @Override
    public void onComplete() {
        LOG.info("onComplete");
    }

    public Future<Connection> getResult() {
        return future;
    }

}
