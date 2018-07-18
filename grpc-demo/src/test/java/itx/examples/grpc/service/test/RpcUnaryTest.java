package itx.examples.grpc.service.test;

import itx.examples.grpc.service.HelloReply;
import itx.examples.grpc.service.SimpleClient;
import itx.examples.grpc.service.SimpleServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class RpcUnaryTest {

    final private static Logger LOG = LoggerFactory.getLogger(RpcUnaryTest.class);

    private static final int SERVER_PORT = 50054;
    private static final String SERVER_HOST = "localhost";
    private static final int MAX_MESSAGES = 10*100;

    @Test
    public void unaryRpcTest() throws IOException, InterruptedException {
        SimpleServer server = null;
        try {
            server = new SimpleServer(SERVER_HOST, SERVER_PORT);
            server.start();
            SimpleClient client = new SimpleClient(SERVER_HOST, SERVER_PORT);
            HelloReply response = client.greet("world");
            Assert.assertNotNull(response);
            Assert.assertEquals("Hello world", response.getMessage());
        } finally {
            if (server != null) {
                server.stop();
                server.blockUntilShutdown();
            }
        }
    }

    @Test
    public void testManyMessages() throws IOException, InterruptedException {
        SimpleServer server = null;
        try {
            server = new SimpleServer(SERVER_HOST, SERVER_PORT);
            server.start();
            SimpleClient client = new SimpleClient(SERVER_HOST, SERVER_PORT);

            //JVM warm-up
            loopMessages(client);
            Thread.sleep(5000);
            long duration = System.currentTimeMillis();
            //measured run
            loopMessages(client);
            duration = System.currentTimeMillis() - duration;

            LOG.info("messages {} send in {} ms", MAX_MESSAGES, duration);
            LOG.info("performance {} msg/sec", (MAX_MESSAGES / (duration / 1000f)));
        } finally {
            if (server != null) {
                server.stop();
                server.blockUntilShutdown();
            }
        }
    }

    private void loopMessages(SimpleClient client) {
        for (int i=0; i<MAX_MESSAGES; i++) {
            HelloReply response = client.greet("msg" + i);
            Assert.assertNotNull(response);
            Assert.assertEquals("Hello msg" + i, response.getMessage());
        }
    }

}
