package itx.examples.jetty.client.http20;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import itx.examples.jetty.client.GetListener;
import itx.examples.jetty.client.HttpAccessException;
import itx.examples.jetty.client.RestClient20;
import itx.examples.jetty.common.dto.EchoMessage;
import itx.examples.jetty.common.services.EchoService;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.http2.api.Session;
import org.eclipse.jetty.http2.api.Stream;
import org.eclipse.jetty.http2.frames.DataFrame;
import org.eclipse.jetty.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class EchoServiceClient extends RestClient20 implements EchoService, AutoCloseable {

    final private static Logger LOG = LoggerFactory.getLogger(EchoServiceClient.class);

    private HttpURI echoServiceURI;
    private ObjectMapper objectMapper;
    private Session session;

    public EchoServiceClient(String host, int port, KeyStore keyStore, String keyStorePassword) {
        super(host, port, keyStore, keyStorePassword);
        this.objectMapper = new ObjectMapper();
        this.echoServiceURI = new HttpURI("https://" + host + ":" + port + "/stream/echo");
    }

    public void connect() throws Exception {
        session = createSession();
    }

    @Override
    public String ping(String message) {
        try {
            GetListener getListener = new GetListener();
            Stream stream = createStream(session, echoServiceURI, getListener);
            return sendMessage(getListener, stream, message, true);
        } catch (Exception e) {
            throw new HttpAccessException(e);
        }
    }

    public List<String> ping(List<String> messages) {
        try {
            List<String> responses = new ArrayList<>();
            GetListener getListener = new GetListener();
            Stream stream = createStream(session, echoServiceURI, getListener);
            for (int i=0; i<messages.size(); i++) {
                boolean endStream = (i == (messages.size() - 1));
                String response = sendMessage(getListener, stream, messages.get(i), endStream);
                responses.add(response);
                getListener.restart();
            }
            return responses;
        } catch (Exception e) {
            throw new HttpAccessException(e);
        }
    }

    @Override
    public void close() throws Exception {
        session.close(0, null, new Callback() {});
        super.close();
    }

    private String sendMessage(GetListener getListener, Stream stream, String message, boolean endStream) throws JsonProcessingException {
        EchoMessage echoMessage = new EchoMessage(message);
        DataFrame dataFrame = new DataFrame(stream.getId(),
                ByteBuffer.wrap(objectMapper.writeValueAsBytes(echoMessage)), endStream);
        stream.data(dataFrame , new Callback() {});
        EchoMessage echoResponse = getListener.get(EchoMessage.class);
        getListener.restart();
        return echoResponse.getMessage();
    }

}
