package itx.examples.grpc.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerBulkDataChannelStreamObserver implements StreamObserver<DataMessages> {

    final private static Logger LOG = LoggerFactory.getLogger(ServerBulkDataChannelStreamObserver.class);

    private final StreamObserver<DataMessages> responseObserver;

    public ServerBulkDataChannelStreamObserver(StreamObserver<DataMessages> responseObserver) {
        LOG.info("BulkDataChannel server observer started.");
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(DataMessages value) {
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
