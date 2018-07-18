package itx.examples.jetty.server.http2;

import org.eclipse.jetty.http.MetaData;
import org.eclipse.jetty.http2.ErrorCode;
import org.eclipse.jetty.http2.IStream;
import org.eclipse.jetty.http2.api.Session;
import org.eclipse.jetty.http2.api.Stream;
import org.eclipse.jetty.http2.api.server.ServerSessionListener;
import org.eclipse.jetty.http2.frames.*;
import org.eclipse.jetty.http2.server.HTTP2ServerConnection;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.io.EofException;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class CustomSessionListener extends ServerSessionListener.Adapter implements Stream.Listener {

    final private static Logger LOG = LoggerFactory.getLogger(CustomSessionListener.class);

    private final Connector connector;
    private final EndPoint endPoint;

    private int maxDynamicTableSize = 4096;
    private int initialSessionRecvWindow = 1024 * 1024;
    private int maxConcurrentStreams = 128;
    private int requestHeaderSize=8*1024;

    private Map<String, StreamProcessorRegistration> streamProcessors;

    public CustomSessionListener(Connector connector, EndPoint endPoint, Map<String, StreamProcessorRegistration> streamProcessors) {
        this.connector = connector;
        this.endPoint = endPoint;
        this.streamProcessors = streamProcessors;
    }

    protected HTTP2ServerConnection getConnection()
    {
        return (HTTP2ServerConnection)endPoint.getConnection();
    }

    @Override
    public Map<Integer, Integer> onPreface(Session session) {
        LOG.info("onPreface");
        Map<Integer, Integer> settings = new HashMap<>();
        settings.put(SettingsFrame.HEADER_TABLE_SIZE, maxDynamicTableSize);
        settings.put(SettingsFrame.INITIAL_WINDOW_SIZE, initialSessionRecvWindow);
        //int maxConcurrentStreams = getMaxConcurrentStreams();
        if (maxConcurrentStreams >= 0) {
            settings.put(SettingsFrame.MAX_CONCURRENT_STREAMS, maxConcurrentStreams);
        }
        settings.put(SettingsFrame.MAX_HEADER_LIST_SIZE, requestHeaderSize);
        return settings;
    }

    @Override
    public Stream.Listener onNewStream(Stream stream, HeadersFrame frame) {
        LOG.info("onNewStream");
        if (frame.getMetaData().isRequest()) {
            MetaData.Request request = (MetaData.Request)frame.getMetaData();
            String uriPath = request.getURI().getPath();
            LOG.info("isRequest {} {}", request.getMethod(), uriPath);
            StreamProcessorRegistration streamProcessorRegistration = streamProcessors.get(uriPath);
            if (streamProcessorRegistration != null) {
                LOG.info("diverting to stream processor {}", uriPath);
                return streamProcessorRegistration.getFactory().create(stream);
            }
        }
        getConnection().onNewStream(connector, (IStream)stream, frame);
        return this;
    }

    @Override
    public boolean onIdleTimeout(Session session) {
        LOG.info("onIdleTimeout");
        boolean close = super.onIdleTimeout(session);
        if (!close)
            return false;

        long idleTimeout = getConnection().getEndPoint().getIdleTimeout();
        return getConnection().onSessionTimeout(new TimeoutException("Session idle timeout " + idleTimeout + " ms"));
    }

    @Override
    public void onClose(Session session, GoAwayFrame frame, Callback callback) {
        LOG.info("onClose");
        ErrorCode error = ErrorCode.from(frame.getError());
        if (error == null) {
            error = ErrorCode.STREAM_CLOSED_ERROR;
        }
        String reason = frame.tryConvertPayload();
        if (reason != null && !reason.isEmpty()) {
            reason = " (" + reason + ")";
        }
        getConnection().onSessionFailure(new EofException("HTTP/2 " + error + reason), callback);
    }

    @Override
    public void onFailure(Session session, Throwable failure, Callback callback) {
        LOG.info("onFailure");
        getConnection().onSessionFailure(failure, callback);
    }

    @Override
    public void onHeaders(Stream stream, HeadersFrame frame) {
        LOG.info("onHeaders");
        if (frame.isEndStream()) {
            getConnection().onTrailers((IStream) stream, frame);
        } else {
            close(stream, "invalid_trailers");
        }
    }

    @Override
    public Stream.Listener onPush(Stream stream, PushPromiseFrame frame) {
        LOG.info("onPush");
        // Servers do not receive pushes.
        close(stream, "push_promise");
        return null;
    }

    @Override
    public void onData(Stream stream, DataFrame frame, Callback callback) {
        LOG.info("onData");
        getConnection().onData((IStream)stream, frame, callback);
    }

    @Override
    public void onReset(Stream stream, ResetFrame frame) {
        LOG.info("onReset");
        ErrorCode error = ErrorCode.from(frame.getError());
        if (error == null) {
            error = ErrorCode.CANCEL_STREAM_ERROR;
        }
        getConnection().onStreamFailure((IStream)stream, new EofException("HTTP/2 " + error), Callback.NOOP);
    }

    @Override
    public boolean onIdleTimeout(Stream stream, Throwable x) {
        LOG.info("onIdleTimeout");
        return getConnection().onStreamTimeout((IStream)stream, x);
    }

    private void close(Stream stream, String reason) {
        LOG.info("close");
        stream.getSession().close(ErrorCode.PROTOCOL_ERROR.code, reason, Callback.NOOP);
    }

}
