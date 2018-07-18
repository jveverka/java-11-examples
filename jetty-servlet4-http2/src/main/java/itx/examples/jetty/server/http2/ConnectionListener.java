package itx.examples.jetty.server.http2;

import org.eclipse.jetty.io.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionListener implements Connection.Listener {

    final private static Logger LOG = LoggerFactory.getLogger(ConnectionListener.class);

    @Override
    public void onOpened(Connection connection) {
        LOG.info("onOpened");
    }

    @Override
    public void onClosed(Connection connection) {
        LOG.info("onClosed");
    }

}
