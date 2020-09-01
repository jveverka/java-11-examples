package itx.examples.r2dbc.services.subscribers;

import itx.examples.r2dbc.dto.UserData;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

public class UserDataListSubscriber implements Subscriber<UserData> {

    private static final Logger LOG = LoggerFactory.getLogger(UserDataListSubscriber.class);

    private final Collection<UserData> userDataCollection;
    private final CountDownLatch cl;

    private Subscription subscription;
    private Throwable throwable;

    public UserDataListSubscriber() {
        this.userDataCollection = new ArrayList<>();
        this.cl = new CountDownLatch(1);
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        LOG.info("onSubscribe");
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(UserData userData) {
        LOG.info("onNext");
        this.userDataCollection.add(userData);
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable t) {
        LOG.info("onError");
        this.throwable = t;
        cl.countDown();
    }

    @Override
    public void onComplete() {
        LOG.info("onComplete");
        cl.countDown();
    }

    public Collection<UserData> waitAndGetResult() throws Throwable {
        cl.await();
        if (throwable != null) {
            throw throwable;
        }
        return userDataCollection;
    }

}
