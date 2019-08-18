package itx.examples.sshd.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import itx.examples.sshd.Main;
import itx.examples.sshd.ServerApp;
import itx.examples.sshd.commands.dto.GetRequest;
import itx.examples.sshd.commands.dto.GetResponse;
import itx.examples.sshd.commands.dto.SetRequest;
import itx.examples.sshd.commands.dto.SetResponse;
import itx.examples.sshd.commands.dto.StartStreamRequest;
import itx.examples.sshd.commands.dto.StreamResponse;
import itx.ssh.client.Client;
import itx.ssh.client.ClientBuilder;
import itx.ssh.client.ServerKeyVerifierBuilder;
import itx.ssh.client.SshSession;
import org.apache.sshd.client.keyverifier.ServerKeyVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class IntegrationTest {

    final private static Logger LOG = LoggerFactory.getLogger(IntegrationTest.class);

    private ServerApp app;
    private Client client;

    @BeforeClass
    public void init() throws CertificateException, InterruptedException,
            UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyStoreException {
        LOG.info("test start");
        this.app = new ServerApp();
        this.app.startApplication();

        InputStream resourceAsStream = Main.class.getClassLoader().getResourceAsStream("client-keystore.jks");
        ServerKeyVerifier serverKeyVerifier = new ServerKeyVerifierBuilder()
                .setIs(resourceAsStream)
                .setPublicKeyAlias("serverkey")
                .setKeystorePassword("secret")
                .build();

        this.client = new ClientBuilder()
                .setHostName("127.0.0.1")
                .setPort(2222).setUserName("user")
                .setPassword("secret")
                .withServerKeyVerifier(serverKeyVerifier)
                .build();
        this.client.start();
    }

    @Test
    public void testSshServerAppClientRequests() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SshSessionListenerImpl sessionListener = new SshSessionListenerImpl();
            SshSession session = client.getSession(sessionListener);

            //Test SetRequest
            CountDownLatch countDownLatch = new CountDownLatch(1);
            sessionListener.reset(countDownLatch);
            SetRequest setRequest = new SetRequest(1, "hello");
            session.send(mapper.writeValueAsBytes(setRequest));
            countDownLatch.await(10, TimeUnit.SECONDS);
            byte[] lastMessage = sessionListener.getLastMessage();
            Assert.assertNotNull(lastMessage);
            SetResponse setResponse = mapper.readValue(lastMessage, SetResponse.class);
            Assert.assertNotNull(setResponse);
            Assert.assertEquals(setRequest.getId(), setResponse.getId());
            Assert.assertEquals(setRequest.getData(), setResponse.getData());

            //Test GetRequest
            countDownLatch = new CountDownLatch(1);
            sessionListener.reset(countDownLatch);
            GetRequest getRequest = new GetRequest(2);
            session.send(mapper.writeValueAsBytes(getRequest));
            countDownLatch.await(10, TimeUnit.SECONDS);
            lastMessage = sessionListener.getLastMessage();
            Assert.assertNotNull(lastMessage);
            GetResponse getResponse = mapper.readValue(lastMessage, GetResponse.class);
            Assert.assertNotNull(getResponse);
            Assert.assertEquals(getRequest.getId(), getResponse.getId());
            Assert.assertEquals(setRequest.getData(), getResponse.getData());

            session.close();
        } catch (Exception e) {
            Assert.fail("Error", e);
        }
    }

    @Test
    public void testSshServerAppPushToServer() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SshSessionListenerImpl sessionListener = new SshSessionListenerImpl();
            SshSession session = client.getSession(sessionListener);

            CountDownLatch countDownLatch = new CountDownLatch(10);
            sessionListener.reset(countDownLatch);
            StartStreamRequest startStreamRequest = new StartStreamRequest(10, 10, "test");
            session.send(mapper.writeValueAsBytes(startStreamRequest));
            countDownLatch.await(10, TimeUnit.SECONDS);
            List<byte[]> messages = sessionListener.getMessages();
            Assert.assertNotNull(messages);
            Assert.assertEquals(messages.size(), 10);

            messages.forEach( m -> {
                try {
                    StreamResponse streamResponse = mapper.readValue(m, StreamResponse.class);
                    Assert.assertEquals(startStreamRequest.getId(), streamResponse.getId());
                    Assert.assertEquals(startStreamRequest.getMessage(), streamResponse.getMessage());
                } catch (IOException e) {
                    Assert.fail("Error", e);
                }
            });

            session.close();
        } catch (Exception e) {
            Assert.fail("Error", e);
        }
    }

    @AfterClass
    public void shutdown() throws Exception {
        LOG.info("test stop");
        if (app != null) {
            app.stop();
        }
        if (client != null) {
            client.close();
        }
    }

}
