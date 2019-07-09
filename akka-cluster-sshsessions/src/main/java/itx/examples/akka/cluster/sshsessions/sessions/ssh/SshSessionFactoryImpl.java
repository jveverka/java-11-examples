package itx.examples.akka.cluster.sshsessions.sessions.ssh;

import itx.examples.akka.cluster.sshsessions.client.HostData;
import itx.examples.akka.cluster.sshsessions.client.SshClientSession;
import itx.examples.akka.cluster.sshsessions.client.SshClientSessionListener;
import itx.examples.akka.cluster.sshsessions.client.UserCredentials;
import itx.examples.akka.cluster.sshsessions.sessions.SshClientException;
import itx.examples.akka.cluster.sshsessions.sessions.SshSessionFactory;

import java.io.IOException;

/**
 * Created by juraj on 25.3.2017.
 */
public class SshSessionFactoryImpl implements SshSessionFactory {

    @Override
    public SshClientSession createSshSession(HostData hostData, UserCredentials userCredentials,
                                             String sessionId, SshClientSessionListener sshClientSessionListener)
            throws SshClientException {
        try {
            SshClientSessionImpl sshClientSession
                    = new SshClientSessionImpl(hostData, userCredentials, sessionId, sshClientSessionListener);
            sshClientSession.connect();
            return sshClientSession;
        } catch (IOException e) {
            throw new SshClientException(e);
        }
    }

}
