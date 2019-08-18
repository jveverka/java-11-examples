package itx.ssh.client;

import java.io.IOException;

/**
 * Main interface of ssh-client instance.
 * Please note that this is not general purpose ssh client.
 * This client is crafted to fit ssh-server APIs and will not work with other ssh servers.
 */
public interface Client extends AutoCloseable {

    /**
     * Start ssh-client, bu do not create connection to server yet.
     */
    void start();

    /**
     * Get connected session to ssh server.
     * @param listener session listener for receiving data from ssh-server.
     * @return {@link SshSession} instance.
     * @throws IOException
     */
    SshSession getSession(SshSessionListener listener) throws IOException;

}
