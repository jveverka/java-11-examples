package itx.examples.akka.cluster.sshsessions.client.impl;

import com.google.common.util.concurrent.SettableFuture;
import itx.examples.akka.cluster.sshsessions.client.SshClientSession;

/**
 * Created by juraj on 3/19/17.
 */
public class SessionCreateInfo {

    private final SettableFuture<SshClientSession> sessionSettableFuture;
    private final SshClientSessionImpl sshClientSession;

    public SessionCreateInfo(SettableFuture<SshClientSession> sessionSettableFuture, SshClientSessionImpl sshClientSession) {
        this.sessionSettableFuture = sessionSettableFuture;
        this.sshClientSession = sshClientSession;
    }

    public SettableFuture<SshClientSession> getSessionSettableFuture() {
        return sessionSettableFuture;
    }

    public SshClientSessionImpl getSshClientSession() {
        return sshClientSession;
    }

}
