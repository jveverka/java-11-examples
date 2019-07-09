package itx.examples.akka.cluster.sshsessions.sessions.ssh;

import itx.examples.akka.cluster.sshsessions.client.HostData;
import itx.examples.akka.cluster.sshsessions.client.SshClientSession;
import itx.examples.akka.cluster.sshsessions.client.SshClientSessionListener;
import itx.examples.akka.cluster.sshsessions.client.UserCredentials;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by juraj on 25.3.2017.
 */
public class SshClientSessionImpl implements SshClientSession {

    private static final Logger LOG = LoggerFactory.getLogger(SshClientSessionImpl.class);

    private HostData hostData;
    private UserCredentials userCredentials;
    private String sessionId;
    private SshClientSessionListener sshClientSessionListener;
    private SshClient client;
    private ClientSession session;

    public SshClientSessionImpl(HostData hostData, UserCredentials userCredentials,
                                String sessionId, SshClientSessionListener sshClientSessionListener) {
        this.hostData = hostData;
        this.userCredentials = userCredentials;
        this.sessionId = sessionId;
        this.sshClientSessionListener = sshClientSessionListener;
    }

    public void connect() throws IOException {
        client = SshClient.setUpDefaultClient();
        client.start();
        session = client.connect(userCredentials.getUserName(),
                hostData.getHostName(), hostData.getPort()).verify().getSession();
        session.addPasswordIdentity(userCredentials.getPassword());
        session.auth().await();
    }

    @Override
    public String getId() {
        return sessionId;
    }

    @Override
    public void sendData(String data) {
        LOG.info("sendData");
    }

    @Override
    public void close() throws Exception {
        session.close();
        client.close();
    }

}
