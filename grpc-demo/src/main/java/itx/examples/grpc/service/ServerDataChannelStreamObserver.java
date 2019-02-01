package itx.examples.grpc.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerDataChannelStreamObserver implements StreamObserver<DataMessage> {

    final private static Logger LOG = LoggerFactory.getLogger(ServerDataChannelStreamObserver.class);

    private final StreamObserver<DataMessage> responseObserver;

    public ServerDataChannelStreamObserver(StreamObserver<DataMessage> responseObserver) {
        LOG.info("DataChannel server observer started.");
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(DataMessage value) {
        responseObserver.onNext(value);
    }

    @Override
    public void onError(Throwable t) {
        responseObserver.onError(t);
    }

    @Override
    public void onCompleted() {
        responseObserver.onCompleted();
    }

}
