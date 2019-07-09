package itx.examples.akka.cluster.sshsessions.tests.mock;

import itx.examples.akka.cluster.sshsessions.client.SshClientSession;
import itx.examples.akka.cluster.sshsessions.client.SshClientSessionListener;
import itx.examples.akka.cluster.sshsessions.dto.SessionDataRequest;
import itx.examples.akka.cluster.sshsessions.dto.SessionDataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by juraj on 3/18/17.
 */
public class SshSessionImpl implements SshClientSession {

    private static final Logger LOG = LoggerFactory.getLogger(SshSessionImpl.class);

    private String id;
    private SshClientSessionListener listener;

    public SshSessionImpl(String id, SshClientSessionListener listener) {
        this.id = id;
        this.listener = listener;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void sendData(String data) {
        if (listener != null) {
            listener.onData(data.toUpperCase());
        }
    }

    @Override
    public void close() throws Exception {
        LOG.info("close: " + id);
    }

    public SessionDataResponse processData(SessionDataRequest sessionDataRequest) {
        return new SessionDataResponse(sessionDataRequest.getData().toUpperCase());
    }

}
