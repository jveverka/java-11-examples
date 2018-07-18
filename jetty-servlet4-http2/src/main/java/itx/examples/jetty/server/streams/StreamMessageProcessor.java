package itx.examples.jetty.server.streams;

import itx.examples.jetty.common.services.MessageServiceAsync;
import org.eclipse.jetty.http2.api.Stream;
import org.eclipse.jetty.http2.frames.DataFrame;
import org.eclipse.jetty.http2.frames.HeadersFrame;
import org.eclipse.jetty.http2.frames.PushPromiseFrame;
import org.eclipse.jetty.http2.frames.ResetFrame;
import org.eclipse.jetty.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamMessageProcessor implements Stream.Listener {

    final private static Logger LOG = LoggerFactory.getLogger(StreamMessageProcessor.class);

    final private MessageServiceAsync messageService;
    final private Stream stream;

    public StreamMessageProcessor(Stream stream, MessageServiceAsync messageService) {
        LOG.info("init ...");
        this.stream = stream;
        this.messageService = messageService;
    }

    @Override
    public void onHeaders(Stream stream, HeadersFrame frame) {

    }

    @Override
    public Stream.Listener onPush(Stream stream, PushPromiseFrame frame) {
        return null;
    }

    @Override
    public void onData(Stream stream, DataFrame frame, Callback callback) {

    }

    @Override
    public void onReset(Stream stream, ResetFrame frame) {

    }

    @Override
    public boolean onIdleTimeout(Stream stream, Throwable x) {
        return false;
    }

}
