package itx.examples.akka.cluster.sshsessions.sessions;

import itx.examples.akka.cluster.sshsessions.client.HostData;
import itx.examples.akka.cluster.sshsessions.client.SshClientSession;
import itx.examples.akka.cluster.sshsessions.client.SshClientSessionListener;
import itx.examples.akka.cluster.sshsessions.client.UserCredentials;

/**
 * Created by juraj on 25.3.2017.
 */
public interface SshSessionFactory {

    SshClientSession createSshSession(HostData hostData, UserCredentials userCredentials,
                                      String sessionId, SshClientSessionListener sshClientSessionListener)
            throws SshClientException;

}
