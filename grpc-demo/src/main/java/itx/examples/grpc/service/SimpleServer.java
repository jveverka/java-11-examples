package itx.examples.grpc.service;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SimpleServer {

    final private static Logger LOG = LoggerFactory.getLogger(SimpleServer.class);

    private Server server;
    private int port = 50051;
    private String host = "127.0.0.1";

    public SimpleServer(String host, int port) {
        if (host != null) {
            this.host = host;
        }
        this.port = port;
    }

    public void start() throws IOException {
        server = NettyServerBuilder.forAddress(new InetSocketAddress(host, port))
        //server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build()
                .start();
        LOG.info("Server started, listening on {}:{}", host, port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    static class GreeterImpl extends GreeterGrpc.GreeterImplBase {

        @Override
        public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
            HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
            LOG.debug("reply: {}", reply.getMessage());
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

        @Override
        public void getData(DataMessage request, StreamObserver<DataMessage> responseObserver) {
            responseObserver.onNext(request);
            responseObserver.onCompleted();
        }

        @Override
        public StreamObserver<DataMessage> dataChannel(StreamObserver<DataMessage> responseObserver) {
            return new ServerDataChannelStreamObserver(responseObserver);
        }

        @Override
        public void getBulkData(DataMessages request, StreamObserver<DataMessages> responseObserver) {
            responseObserver.onNext(request);
            responseObserver.onCompleted();
        }

        @Override
        public StreamObserver<DataMessages> bulkDataChannel(StreamObserver<DataMessages> responseObserver) {
            return new ServerBulkDataChannelStreamObserver(responseObserver);
        }

    }

}
