package itx.examples.akka.cluster.sshsessions.tests.mock;

import itx.examples.akka.cluster.sshsessions.client.HostData;
import itx.examples.akka.cluster.sshsessions.client.SshClientSession;
import itx.examples.akka.cluster.sshsessions.client.SshClientSessionListener;
import itx.examples.akka.cluster.sshsessions.client.UserCredentials;
import itx.examples.akka.cluster.sshsessions.sessions.SshClientException;
import itx.examples.akka.cluster.sshsessions.sessions.SshSessionFactory;

/**
 * Created by juraj on 25.3.2017.
 */
public class SshSessionFactoryImpl implements SshSessionFactory {

    @Override
    public SshClientSession createSshSession(HostData hostData, UserCredentials userCredentials,
                                             String sessionId, SshClientSessionListener sshClientSessionListener)
            throws SshClientException {
        return new SshSessionImpl(sessionId, sshClientSessionListener);
    }

}
