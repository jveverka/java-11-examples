package itx.examples.akka.cluster.sshsessions.tests;

import com.google.common.util.concurrent.ListenableFuture;
import itx.examples.akka.cluster.sshsessions.Application;
import itx.examples.akka.cluster.sshsessions.client.HostData;
import itx.examples.akka.cluster.sshsessions.client.SshClientService;
import itx.examples.akka.cluster.sshsessions.client.SshClientSession;
import itx.examples.akka.cluster.sshsessions.client.UserCredentials;
import itx.examples.akka.cluster.sshsessions.tests.mock.SshSessionFactoryImpl;
import itx.examples.akka.cluster.sshsessions.sessions.SshSessionFactory;
import itx.examples.akka.cluster.sshsessions.tests.utils.AkkaTestCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by juraj on 3/18/17.
 */
public class ClusterBaseTest {

    private static final Logger LOG = LoggerFactory.getLogger(ClusterBaseTest.class);
    private static final int CLUSTER_SIZE = 3;

    private AkkaTestCluster akkaTestCluster;
    private HostData hostData = new HostData("127.0.0.1", 2222);
    private UserCredentials userCredentials = new UserCredentials("admin", "admin");

    @BeforeClass
    private void init() throws IOException {
        LOG.info("test init");
        SshSessionFactory sshSessionFactory = new SshSessionFactoryImpl();
        akkaTestCluster = new AkkaTestCluster(CLUSTER_SIZE);
        akkaTestCluster.startCluster(20, TimeUnit.SECONDS);
        for (int i=0; i<CLUSTER_SIZE; i++) {
            Application application = new Application(akkaTestCluster.getClusterObjectRegistry(i).getActorSystem(), sshSessionFactory);
            application.init();
            akkaTestCluster.getClusterObjectRegistry(i).registerSingleObject(Application.class, application);
        }
    }

    @Test
    public void testCreateSshSession() throws InterruptedException {
        LOG.info("testCreateSshSession");
        SshClientService sshClientService1 = akkaTestCluster.getClusterObjectRegistry(0)
                .getSingleObject(Application.class).getSshClientService();
        SshClientSessionListenerTestImpl sshClientSessionListenerTest = new SshClientSessionListenerTestImpl();
        try {
            ListenableFuture<SshClientSession> upcommingSession = sshClientService1.createSession(hostData, userCredentials,
                    sshClientSessionListenerTest, 5, TimeUnit.SECONDS);
            long duration = System.nanoTime();
            SshClientSession sshClientSession = upcommingSession.get(20, TimeUnit.SECONDS);
            duration = System.nanoTime() - duration;
            LOG.info("session created in " + duration + " ns");
            String sessionId = sshClientSession.getId();
            Assert.assertNotNull(sessionId);
            duration = System.nanoTime();
            sshClientSession.sendData("data");
            String data = sshClientSessionListenerTest.waitForData(20, TimeUnit.SECONDS);
            duration = System.nanoTime() - duration;
            LOG.info("session data transfer in " + duration + " ns");
            Assert.assertEquals(data, "DATA");
            sshClientSession.close();
        } catch (ExecutionException e) {
            LOG.error("Execution Exception: ", e.getCause().getMessage());
            Assert.fail();
        } catch (TimeoutException e) {
            LOG.error("Test TIMEOUT");
            Assert.fail();
        } catch (Exception e) {
            LOG.error("Exception: ", e);
            Assert.fail();
        }
    }

    @Test
    public void testCreateManySessions() {
        LOG.info("testCreateSshSession");
        SshClientService sshClientService1 = akkaTestCluster.getClusterObjectRegistry(0)
                .getSingleObject(Application.class).getSshClientService();
        SshClientSessionListenerTestImpl sshClientSessionListenerTest = new SshClientSessionListenerTestImpl();
        List<SshClientSession> sessions = new ArrayList<>();
        for (int i=0; i<10; i++) {
            try {
                ListenableFuture<SshClientSession> upcommingSession = sshClientService1.createSession(hostData, userCredentials,
                        sshClientSessionListenerTest, 5, TimeUnit.SECONDS);
                SshClientSession sshClientSession = upcommingSession.get(20, TimeUnit.SECONDS);
                Assert.assertNotNull(sshClientSession);
                sshClientSessionListenerTest.setDataWait();
                sshClientSession.sendData("data" + i);
                String data = sshClientSessionListenerTest.waitForData(20, TimeUnit.SECONDS);
                Assert.assertEquals(data, "DATA" + i);
            } catch (InterruptedException e) {
                LOG.error("InterruptedException: ");
                Assert.fail();
            } catch (ExecutionException e) {
                LOG.error("Execution Exception: ", e.getCause().getMessage());
                Assert.fail();
            } catch (TimeoutException e) {
                LOG.error("Test TIMEOUT");
                Assert.fail();
            } catch (Exception e) {
                LOG.error("Exception: ", e);
                Assert.fail();
            }

        }
        for (SshClientSession session: sessions) {
            try {
                session.close();
            } catch (Exception e) {
                LOG.error("Exception: ", e);
                Assert.fail();
            }
        }
    }

    @AfterClass
    private void destroy() {
        try {
            for (int i=0; i<CLUSTER_SIZE; i++) {
                Application application = akkaTestCluster.getClusterObjectRegistry(i).getSingleObject(Application.class);
                application.destroy();
            }
            akkaTestCluster.stopCluster();
        } catch (Exception e) {
            LOG.error("Akka cluster shutdown failed !", e);
        }
    }

}
