package itx.ssh.server.commands.subsystem;

import java.io.IOException;

/**
 * Represents ssh-client session on server.
 * Used to dispatch messages to ssh-client or close underlying client session.
 */
public interface SshClientSession {

    /**
     * Get session ID fot this client.
     * @return
     */
    long getSessionId();

    /**
     * send message to the client.
     * @param message message payload data.
     * @throws IOException
     */
    void sendMessage(byte[] message) throws IOException;

    /**
     * close underlying client session.
     * @param returnCode
     * @param message
     */
    void close(int returnCode, String message);

}
