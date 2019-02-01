package itx.examples.grpc.service;

import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ClientBulkDataChannelStreamObserver implements StreamObserver<DataMessages> {

    private final CountDownLatch countDownLatch;

    public ClientBulkDataChannelStreamObserver(int expectedMessages) {
        this.countDownLatch = new CountDownLatch(expectedMessages);
    }

    @Override
    public void onNext(DataMessages value) {
        for (int i = 0; i<value.getMessagesCount(); i++) {
            countDownLatch.countDown();
        }
    }

    @Override
    public void onError(Throwable t) {
        while (countDownLatch.getCount() > 0) {
            countDownLatch.countDown();
        }
    }

    @Override
    public void onCompleted() {
        while (countDownLatch.getCount() > 0) {
            countDownLatch.countDown();
        }
    }

    public void awaitCompletion() throws InterruptedException {
        countDownLatch.await();
    }

    public void awaitCompletion(long timeout, TimeUnit timeUnit) throws InterruptedException {
        countDownLatch.await(timeout, timeUnit);
    }

}
