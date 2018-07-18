package itx.examples.grpc.service.test;

import io.grpc.stub.StreamObserver;
import itx.examples.grpc.service.DataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class TestDataChannelStreamObserver<V extends DataMessage> implements StreamObserver<V> {

    final private static Logger LOG = LoggerFactory.getLogger(TestDataChannelStreamObserver.class);

    private DataMessage lastMessage;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public void onNext(V value) {
        LOG.info("onNext: {}", value.getMessage());
        lastMessage = value;
        countDownLatch.countDown();
    }

    @Override
    public void onError(Throwable t) {
        LOG.info("onError");
        countDownLatch.countDown();
    }

    @Override
    public void onCompleted() {
        LOG.info("onCompleted");
        countDownLatch.countDown();
    }

    public DataMessage getLastMessage() {
        return lastMessage;
    }

    public void await() throws InterruptedException {
        countDownLatch.await();
    }

}
