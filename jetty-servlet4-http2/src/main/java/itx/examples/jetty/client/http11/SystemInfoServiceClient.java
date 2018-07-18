package itx.examples.jetty.client.http11;

import com.fasterxml.jackson.databind.ObjectMapper;
import itx.examples.jetty.client.HttpAccessException;
import itx.examples.jetty.client.RestClient11;
import itx.examples.jetty.common.dto.SystemInfo;
import itx.examples.jetty.common.services.SystemInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyStore;

public class SystemInfoServiceClient extends RestClient11 implements SystemInfoService {

    final private static Logger LOG = LoggerFactory.getLogger(SystemInfoServiceClient.class);

    final private ObjectMapper mapper;

    public SystemInfoServiceClient(String url) {
        super(url);
        this.mapper = new ObjectMapper();
    }

    public SystemInfoServiceClient(String url, KeyStore keyStore, String keystorePassword) {
        super(url, keyStore, keystorePassword);
        this.mapper = new ObjectMapper();
    }

    @Override
    public SystemInfo getSystemInfo() {
        try {
            String requestURL = getBaseURL() + "/data/system/info";
            LOG.info("getSystemInfo: {}", requestURL);
            return mapper.readValue(getClient().GET(requestURL).getContent(), SystemInfo.class);
        } catch (Exception e) {
            throw new HttpAccessException(e);
        }
    }

}
