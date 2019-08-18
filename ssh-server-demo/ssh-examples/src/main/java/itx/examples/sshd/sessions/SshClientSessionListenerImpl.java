package itx.examples.sshd.sessions;

import itx.ssh.server.commands.subsystem.SshClientSession;
import itx.ssh.server.commands.subsystem.SshClientSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SshClientSessionListenerImpl implements SshClientSessionListener {

    final private static Logger LOG = LoggerFactory.getLogger(SshClientSessionListenerImpl.class);

    private final Map<Long, SshClientSession> sessions;

    public SshClientSessionListenerImpl() {
        this.sessions = new HashMap<>();
    }

    @Override
    public void onNewSession(SshClientSession sshClientSession) {
        LOG.info("onNewSession: {}", sshClientSession.getSessionId());
        sessions.put(sshClientSession.getSessionId(), sshClientSession);
    }

    public SshClientSession getSession(long sessionId) {
        return sessions.get(sessionId);
    }

}
