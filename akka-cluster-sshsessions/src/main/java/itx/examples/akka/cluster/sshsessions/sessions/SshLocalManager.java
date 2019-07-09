package itx.examples.akka.cluster.sshsessions.sessions;

import itx.examples.akka.cluster.sshsessions.dto.ActiveSession;

import java.util.List;

/**
 * Created by juraj on 25.3.2017.
 */
public interface SshLocalManager {

    public List<ActiveSession> getActiveSessions();

}
