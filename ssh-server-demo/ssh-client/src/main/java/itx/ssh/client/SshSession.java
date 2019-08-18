package itx.ssh.client;

import java.io.IOException;

/**
 * Represents open bidirectional connection between ssh-client and ssh-server.
 */
public interface SshSession extends AutoCloseable {

    /**
     * Sends byte array message to ssh-server.
     * @param message massage payload to be send to server.
     * @throws IOException
     */
    void send(byte[] message) throws IOException;

}
