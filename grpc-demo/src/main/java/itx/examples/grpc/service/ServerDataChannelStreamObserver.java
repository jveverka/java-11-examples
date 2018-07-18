package itx.examples.grpc.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerDataChannelStreamObserver<V extends DataMessage> implements StreamObserver<V> {

    final private static Logger LOG = LoggerFactory.getLogger(ServerDataChannelStreamObserver.class);
    final private static String CLOSE_COMMAND = "CLOSE";

    private StreamObserver<DataMessage> responseObserver;
    private ExecutorService executorService;

    public ServerDataChannelStreamObserver(StreamObserver<DataMessage> responseObserver) {
        LOG.info("server observer started.");
        this.responseObserver = responseObserver;
        this.executorService = Executors.newFixedThreadPool(8);
    }

    @Override
    public void onNext(V value) {
        if (CLOSE_COMMAND.equals(value.getMessage())) {
            try {
                responseObserver.onNext(DataMessage.newBuilder().mergeFrom(value).build());
                executorService.shutdown();
                executorService.awaitTermination(20, TimeUnit.SECONDS);
                responseObserver.onCompleted();
                LOG.info("closing server observer.");
            } catch (InterruptedException e) {
                LOG.error("InterruptedException: ", e);
            }
        } else {
            executorService.submit(new ServerReplyTask(responseObserver, value));
        }
    }

    @Override
    public void onError(Throwable t) {
        LOG.info("onError");
    }

    @Override
    public void onCompleted() {
        LOG.info("onCompleted");
    }

}
