package itx.examples.jetty.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import itx.examples.jetty.common.dto.SystemInfo;
import org.eclipse.jetty.http2.api.Stream;
import org.eclipse.jetty.http2.frames.DataFrame;
import org.eclipse.jetty.http2.frames.HeadersFrame;
import org.eclipse.jetty.http2.frames.PushPromiseFrame;
import org.eclipse.jetty.http2.frames.ResetFrame;
import org.eclipse.jetty.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.CountDownLatch;

public class GetListener implements Stream.Listener {

    final private static Logger LOG = LoggerFactory.getLogger(GetListener.class);

    private CountDownLatch countDownLatch;
    private byte[] bytes;
    private ObjectMapper mapper;

    public GetListener() {
        this.countDownLatch = new CountDownLatch(1);
        this.mapper = new ObjectMapper();
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
        LOG.info("onData");
        bytes = new byte[frame.getData().remaining()];
        frame.getData().get(bytes);
        countDownLatch.countDown();
        callback.succeeded();
    }

    @Override
    public void onReset(Stream stream, ResetFrame frame) {
        LOG.info("onReset");
    }

    public void restart() {
        this.countDownLatch.countDown();
        this.countDownLatch = new CountDownLatch(1);
    }

    public <T> T get(Class<T> clazz) {
        try {
            countDownLatch.await();
            return mapper.readValue(bytes, clazz);
        } catch (Exception e) {
            throw new HttpAccessException(e);
        }
    }

}
