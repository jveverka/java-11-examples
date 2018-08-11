package itx.ssh.client;

import java.io.IOException;

public interface SshSession extends AutoCloseable {

    void send(Message request) throws IOException;

}
