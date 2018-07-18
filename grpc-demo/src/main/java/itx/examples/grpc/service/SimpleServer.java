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
        public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
            HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
            LOG.debug("reply: {}", reply.getMessage());
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

        @Override
        public void getData(DataMessage req, StreamObserver<DataMessage> responseObserver) {
            DataMessage reply = DataMessage.newBuilder().mergeFrom(req).build();
            LOG.debug("reply: {} {}", reply.getIndex(), reply.getMessage());
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

        @Override
        public StreamObserver<DataMessage> dataChannel(StreamObserver<DataMessage> responseObserver) {
            return new ServerDataChannelStreamObserver<DataMessage>(responseObserver);
        }

    }

}
