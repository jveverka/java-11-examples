package itx.futures.demo.test;

import itx.futures.demo.DataProviderReactive;
import itx.futures.demo.DataProviderReactiveImpl;
import itx.futures.demo.dto.FlowData;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.Flow;

public class DataReactiveTest {

    private DataProviderReactive dataProviderReactive;

    @BeforeClass
    public void setup() {
        dataProviderReactive = new DataProviderReactiveImpl();
    }

    @Test
    public void testSubscription() {
        TestSubscriber testSubscriber = new TestSubscriber();
        dataProviderReactive.subscribe(testSubscriber);
        testSubscriber.close();
    }

    @AfterClass
    public void shutdown() throws Exception {
        dataProviderReactive.close();
    }

    private final class TestSubscriber implements Flow.Subscriber<FlowData> {

        private Flow.Subscription subscription;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
        }

        @Override
        public void onNext(FlowData item) {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {

        }

        public void close() {
            subscription.cancel();
        }

        public void request(long n) {
            subscription.request(n);
        }

    }

}
