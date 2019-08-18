package itx.ssh.client;

/**
 * Interface for async notifications from underlying ssh session.
 */
public interface SshSessionListener {

    /**
     * Provides received message from ssh-server.
     * @param message
     */
    void onMessage(byte[] message);

    /**
     * Callback announcing session close event.
     * Related session should not be used anymore.
     */
    void onSessionClosed();

}
