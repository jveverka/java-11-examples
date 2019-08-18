package itx.ssh.server.commands.subsystem;

/**
 * Registration of {@link SshClientSession}
 */
public interface SshClientSessionListener {

    /**
     * When ready, instance of message dispatcher is provided to user.
     * @param sshClientMessageDispatcher
     */
    void onNewSession(SshClientSession sshClientMessageDispatcher);

}
