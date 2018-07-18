package itx.examples.grpc.service;

import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ClientDataChannelStreamObserver<V extends DataMessage> implements StreamObserver<V> {

    private boolean[] msgConfirmation;
    private CountDownLatch countDownLatch;

    public ClientDataChannelStreamObserver(boolean[] msgConfirmation) {
        this.msgConfirmation = msgConfirmation;
        this.countDownLatch = new CountDownLatch(msgConfirmation.length);
    }

    @Override
    public void onNext(V value) {
        int index = (int)value.getIndex();
        msgConfirmation[index] = true;
        countDownLatch.countDown();
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
