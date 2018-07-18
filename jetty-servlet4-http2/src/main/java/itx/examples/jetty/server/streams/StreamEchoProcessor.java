package itx.examples.jetty.server.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import itx.examples.jetty.common.dto.EchoMessage;
import itx.examples.jetty.common.services.EchoService;
import org.eclipse.jetty.http2.api.Stream;
import org.eclipse.jetty.http2.frames.DataFrame;
import org.eclipse.jetty.http2.frames.HeadersFrame;
import org.eclipse.jetty.http2.frames.PushPromiseFrame;
import org.eclipse.jetty.http2.frames.ResetFrame;
import org.eclipse.jetty.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

public class StreamEchoProcessor implements Stream.Listener {

    final private static Logger LOG = LoggerFactory.getLogger(StreamEchoProcessor.class);

    private ObjectMapper objectMapper;
    private EchoService echoService;

    public StreamEchoProcessor(EchoService echoService) {
        LOG.info("init ...");
        this.echoService = echoService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onHeaders(Stream stream, HeadersFrame frame) {
        LOG.info("onHeaders");
    }

    @Override
    public Stream.Listener onPush(Stream stream, PushPromiseFrame frame) {
        LOG.info("onPush");
        return null;
    }

    @Override
    public void onData(Stream stream, DataFrame frame, Callback callback) {
        LOG.info("onData {}", stream.getId());
        try {
            boolean endStream = frame.isEndStream();
            byte[] bytes = new byte[frame.getData().remaining()];
            frame.getData().get(bytes);
            EchoMessage echoMessage = objectMapper.readValue(bytes, EchoMessage.class);
            LOG.info("got echo {}", echoMessage.getMessage());
            String response = echoService.ping(echoMessage.getMessage());
            EchoMessage echoResponse = new EchoMessage(response);
            LOG.info("echo response {}", echoResponse.getMessage());
            DataFrame responseFrame = new DataFrame(stream.getId(),
                    ByteBuffer.wrap(objectMapper.writeValueAsBytes(echoResponse)), endStream);
            stream.data(responseFrame, new Callback() {});
            callback.succeeded();
            LOG.info("echo done");
        } catch (IOException e) {
            LOG.error("IOException: ", e);
        }
    }

    @Override
    public void onReset(Stream stream, ResetFrame frame) {
        LOG.info("onReset");
    }

    @Override
    public boolean onIdleTimeout(Stream stream, Throwable x) {
        LOG.info("onIdleTimeout");
        return false;
    }

}
