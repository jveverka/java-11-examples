package itx.examples.grpc.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SimpleClient {

    final private static Logger LOG = LoggerFactory.getLogger(SimpleClient.class);

    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;
    private final GreeterGrpc.GreeterStub greeterStub;

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public SimpleClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build());
    }

    private SimpleClient(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = GreeterGrpc.newBlockingStub(channel);
        this.greeterStub = GreeterGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public HelloReply greet(String name) {
        LOG.debug("Will try to greet " + name + " ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
            LOG.debug("Greeting: " + response.getMessage());
            return response;
        } catch (StatusRuntimeException e) {
            LOG.warn("RPC failed: {}", e.getStatus());
            return null;
        }
    }

    public DataMessage getData(DataMessage dataMessage) {
        return blockingStub.getData(dataMessage);
    }

    public DataMessages getBulkData(DataMessages dataMessages) {
        return blockingStub.getBulkData(dataMessages);
    }

    public StreamObserver<DataMessage> getDataChannel(StreamObserver<DataMessage> responseObserver) {
        return greeterStub.dataChannel(responseObserver);
    }

    public StreamObserver<DataMessages> getBulkDataChannel(StreamObserver<DataMessages> responseObserver) {
        return greeterStub.bulkDataChannel(responseObserver);
    }

}
