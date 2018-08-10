package itx.examples.sshd.tests;

import itx.examples.sshd.ServerApp;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.session.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

public class IntegrationTest {

    final private static Logger LOG = LoggerFactory.getLogger(IntegrationTest.class);

    private ServerApp app;
    private SshClient client;

    @BeforeClass
    public void init() throws CertificateException, InterruptedException,
            UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyStoreException {
        LOG.info("test start");
        this.app = new ServerApp();
        this.app.startApplication();
        this.client = SshClient.setUpDefaultClient();
        this.client.start();
    }

    @Test
    public void testSshServerApplication() {
        try {
            ClientSession session = client.connect("user", "127.0.0.1", 2222)
                    .verify().getSession();
            session.addPasswordIdentity("secret");
            session.auth().verify(15, TimeUnit.SECONDS);

            ClientChannel channel = session.createChannel(ClientChannel.CHANNEL_SHELL);
            channel.open();
            channel.close();

            session.close();
        } catch (IOException e) {
            Assert.fail();
        }

    }

    @AfterClass
    public void shutdown() throws IOException {
        LOG.info("test stop");
        if (app != null) {
            app.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

}
