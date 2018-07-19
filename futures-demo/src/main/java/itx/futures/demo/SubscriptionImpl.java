package itx.futures.demo;

import java.util.concurrent.Flow;

public class SubscriptionImpl implements Flow.Subscription {

    private DataProviderReactiveImpl dataProviderReactive;

    public SubscriptionImpl(DataProviderReactiveImpl dataProviderReactive) {
        this.dataProviderReactive = dataProviderReactive;
    }

    @Override
    public void request(long n) {

    }

    @Override
    public void cancel() {

    }

}
