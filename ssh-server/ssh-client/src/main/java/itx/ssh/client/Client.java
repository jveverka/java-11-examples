package itx.ssh.client;

import java.io.IOException;

public interface Client extends AutoCloseable {

    void start();

    SshSession getSession(SshSessionListener listener) throws IOException;

}
