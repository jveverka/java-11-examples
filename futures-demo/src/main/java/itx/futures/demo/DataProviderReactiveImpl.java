package itx.futures.demo;

import itx.futures.demo.dto.FlowData;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Flow;

public class DataProviderReactiveImpl implements DataProviderReactive {

    private Set<Flow.Subscriber<? super FlowData>> subscribers;

    public DataProviderReactiveImpl() {
        subscribers = new HashSet<>();
    }

    @Override
    public void subscribe(Flow.Subscriber<? super FlowData> subscriber) {
        subscribers.add(subscriber);
        Flow.Subscription subscription = new SubscriptionImpl(this);
        subscriber.onSubscribe(subscription);
    }

    @Override
    public void close() throws Exception {

    }

}
