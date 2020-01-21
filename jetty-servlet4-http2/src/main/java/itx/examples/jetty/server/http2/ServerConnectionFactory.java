package itx.examples.jetty.server.http2;

import org.eclipse.jetty.http2.FlowControlStrategy;
import org.eclipse.jetty.http2.HTTP2Connection;
import org.eclipse.jetty.http2.api.server.ServerSessionListener;
import org.eclipse.jetty.http2.generator.Generator;
import org.eclipse.jetty.http2.parser.RateControl;
import org.eclipse.jetty.http2.parser.ServerParser;
import org.eclipse.jetty.http2.server.HTTP2ServerConnection;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.http2.server.HTTP2ServerSession;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServerConnectionFactory extends HTTP2ServerConnectionFactory {

    final private static Logger LOG = LoggerFactory.getLogger(ServerConnectionFactory.class);

    private ConnectionListener connectionListener;
    private Map<String, StreamProcessorRegistration> streamProcessors;
    private Executor executor;

    public ServerConnectionFactory(HttpConfiguration httpConfiguration, Map<String, StreamProcessorRegistration> streamProcessors) {
        super(httpConfiguration);
        this.connectionListener = new ConnectionListener();
        this.streamProcessors = streamProcessors;
        this.executor = Executors.newFixedThreadPool(8);
    }

    @Override
    public Connection newConnection(Connector connector, EndPoint endPoint) {
        LOG.info("newConnection: {}", endPoint.getLocalAddress().toString());
        ServerSessionListener listener = new CustomSessionListener(connector, endPoint, streamProcessors);

        Generator generator = new Generator(connector.getByteBufferPool(), getMaxDynamicTableSize(), getMaxHeaderBlockFragment());
        FlowControlStrategy flowControl = getFlowControlStrategyFactory().newFlowControlStrategy();
        HTTP2ServerSession session = new HTTP2ServerSession(connector.getScheduler(), endPoint, generator, listener, flowControl);
        session.setMaxLocalStreams(getMaxConcurrentStreams());
        session.setMaxRemoteStreams(getMaxConcurrentStreams());
        // For a single stream in a connection, there will be a race between
        // the stream idle timeout and the connection idle timeout. However,
        // the typical case is that the connection will be busier and the
        // stream idle timeout will expire earlier than the connection's.
        long streamIdleTimeout = getStreamIdleTimeout();
        if (streamIdleTimeout <= 0) {
            streamIdleTimeout = endPoint.getIdleTimeout();
        }
        session.setStreamIdleTimeout(streamIdleTimeout);
        session.setInitialSessionRecvWindow(getInitialSessionRecvWindow());

        ServerParser parser = newServerParser(connector, session, RateControl.NO_RATE_CONTROL);
        HTTP2Connection connection = new HTTP2ServerConnection(connector.getByteBufferPool(), executor,
                endPoint, getHttpConfiguration(), parser, session, getInputBufferSize(), listener);
        connection.addListener(connectionListener);
        return configure(connection, connector, endPoint);
    }

}
