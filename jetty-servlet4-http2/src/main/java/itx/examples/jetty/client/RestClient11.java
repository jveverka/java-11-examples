package itx.examples.jetty.client;

import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.jetty.client.HttpClient;

import java.security.KeyStore;

public abstract class RestClient11 implements AutoCloseable {

    final private static Logger LOG = LoggerFactory.getLogger(RestClient11.class);

    private String url;
    private HttpClient httpClient;

    public RestClient11(String url) {
        try {
            this.url = url;
            this.httpClient = new HttpClient();
            LOG.info("initializing HTTP client");
            httpClient = new HttpClient();
            httpClient.start();
        } catch (Exception e) {
            LOG.error("RestClient init ERROR: ", e);
        }
    }

    public RestClient11(String url, KeyStore keyStore, String keystorePassword) {
        try {
            this.url = url;
            this.httpClient = new HttpClient();
            if (url.startsWith("https://")) {
                LOG.info("initializing HTTPS client");
                SslContextFactory sslContextFactory = new SslContextFactory();
                sslContextFactory.setTrustStore(keyStore);
                sslContextFactory.setTrustStorePassword(keystorePassword);
                httpClient = new HttpClient(sslContextFactory);
            } else {
                LOG.info("initializing HTTP client");
                httpClient = new HttpClient();
            }
            httpClient.start();
        } catch (Exception e) {
            LOG.error("RestClient init ERROR: ", e);
        }
    }

    public String getBaseURL() {
        return url;
    }

    public HttpClient getClient() {
        return httpClient;
    }

    @Override
    public void close() {
        try {
            httpClient.stop();
        } catch (Exception e) {
            LOG.error("RestClient shutdown ERROR: ", e);
        }
    }

}
