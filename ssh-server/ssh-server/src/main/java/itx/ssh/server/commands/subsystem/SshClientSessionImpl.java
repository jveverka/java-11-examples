package itx.ssh.server.commands.subsystem;

import org.apache.sshd.server.ExitCallback;

import java.io.IOException;
import java.io.OutputStream;

public class SshClientSessionImpl implements SshClientSession {

    private static final int ENTER = 13;

    private final long sessionId;
    private final OutputStream stdout;
    private final ExitCallback exitCallback;

    public SshClientSessionImpl(long sessionId, OutputStream stdout, ExitCallback exitCallback) {
        this.sessionId = sessionId;
        this.stdout = stdout;
        this.exitCallback = exitCallback;
    }

    @Override
    public long getSessionId() {
        return sessionId;
    }

    @Override
    public void sendMessage(byte[] message) throws IOException {
        stdout.write(message);
        stdout.write(ENTER);
        stdout.flush();
    }

    @Override
    public void close(int returnCode, String message) {
        exitCallback.onExit(returnCode, message);
    }

}
