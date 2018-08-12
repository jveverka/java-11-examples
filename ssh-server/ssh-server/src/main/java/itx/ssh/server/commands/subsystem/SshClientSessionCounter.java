package itx.ssh.server.commands.subsystem;

import java.util.concurrent.atomic.AtomicLong;

public class SshClientSessionCounter {

    private final AtomicLong sessionIdCounter;

    public SshClientSessionCounter() {
        this.sessionIdCounter = new AtomicLong(0);
    }

    public long getNewSessionId() {
        return sessionIdCounter.getAndIncrement();
    }

}
