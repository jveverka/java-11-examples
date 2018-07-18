package itx.examples.jetty.client.http11;

import com.fasterxml.jackson.databind.ObjectMapper;
import itx.examples.jetty.client.HttpAccessException;
import itx.examples.jetty.client.RestClient11;
import itx.examples.jetty.common.dto.EchoMessage;
import itx.examples.jetty.common.services.EchoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyStore;

public class EchoServiceClient extends RestClient11 implements EchoService {

    final private static Logger LOG = LoggerFactory.getLogger(EchoServiceClient.class);

    final private ObjectMapper mapper;

    public EchoServiceClient(String url) {
        super(url);
        this.mapper = new ObjectMapper();
    }

    public EchoServiceClient(String url, KeyStore keyStore, String keystorePassword) {
        super(url, keyStore, keystorePassword);
        this.mapper = new ObjectMapper();
    }

    @Override
    public String ping(String message) {
        try {
            String requestURL = getBaseURL() + "/data/echo/" + message;
            LOG.info("ping: {}", requestURL);
            EchoMessage echoMessage = mapper.readValue(getClient().GET(requestURL).getContent(), EchoMessage.class);
            return echoMessage.getMessage();
        } catch (Exception e) {
            throw new HttpAccessException(e);
        }
    }

}
