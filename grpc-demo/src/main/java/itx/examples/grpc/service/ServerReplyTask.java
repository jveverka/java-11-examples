package itx.examples.grpc.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class ServerReplyTask implements Callable<Boolean> {

    final private static Logger LOG = LoggerFactory.getLogger(ServerReplyTask.class);

    private StreamObserver<DataMessage> responseObserver;
    private DataMessage value;

    public ServerReplyTask(StreamObserver<DataMessage> responseObserver, DataMessage value) {
        this.responseObserver = responseObserver;
        this.value = value;
    }

    @Override
    public Boolean call() throws Exception {
        LOG.debug("onNext: {} {}", value.getIndex(), value.getMessage());
        DataMessage dataMessage = DataMessage.newBuilder().mergeFrom(value).build();
        synchronized (responseObserver) {
            responseObserver.onNext(dataMessage);
        }
        return true;
    }

}
