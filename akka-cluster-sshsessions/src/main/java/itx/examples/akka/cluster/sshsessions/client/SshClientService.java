package itx.examples.akka.cluster.sshsessions.client;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.TimeUnit;

/**
 * Created by juraj on 3/19/17.
 */
public interface SshClientService {

    /**
     * ask client service to create ssh session on client's behalf
     * @param hostData
     *  host name and port of target device
     * @param userCredentials
     *  user credentials
     * @param sshClientSessionListener
     *  client's listener for important session events
     * @param timeout
     *  wait for session creation with timeout
     * @param timeUnit
     *  timeout unit for session creation
     * @return
     *  future object representing upcomming ssh session or execution exception
     */
    ListenableFuture<SshClientSession> createSession(HostData hostData,
                                                     UserCredentials userCredentials,
                                                     SshClientSessionListener sshClientSessionListener,
                                                     long timeout, TimeUnit timeUnit);

}
