package itx.examples.jetty.client;

import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http.MetaData;
import org.eclipse.jetty.http2.api.Session;
import org.eclipse.jetty.http2.api.Stream;
import org.eclipse.jetty.http2.api.server.ServerSessionListener;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.frames.HeadersFrame;
import org.eclipse.jetty.util.FuturePromise;
import org.eclipse.jetty.util.Jetty;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class RestClient20 implements AutoCloseable {

    final private static Logger LOG = LoggerFactory.getLogger(RestClient20.class);
    final public static String USER_AGENT = "User-Agent";
    final public static String USER_AGENT_VERSION = "RestClient20/" + Jetty.VERSION;

    private HTTP2Client client;
    private InetSocketAddress address;
    private SslContextFactory sslContextFactory;

    public RestClient20(String host, int port, KeyStore keyStore, String keyStorePassword) {
        try {
            this.client = new HTTP2Client();
            this.address = new InetSocketAddress(host, port);
            sslContextFactory = new SslContextFactory();
            sslContextFactory.setTrustStore(keyStore);
            sslContextFactory.setTrustStorePassword(keyStorePassword);
            client.addBean(sslContextFactory);
            client.start();
        } catch (Exception e) {
            LOG.error("Exception: ", e);
        }
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public HTTP2Client getClient() {
        return client;
    }

    public Session createSession() throws InterruptedException, ExecutionException, TimeoutException {
        FuturePromise<Session> sessionPromise = new FuturePromise<>();
        client.connect(sslContextFactory, address, new ServerSessionListener.Adapter(), sessionPromise);
        Session session = sessionPromise.get(5, TimeUnit.SECONDS);
        return session;
    }

    public Stream createStream(Session session, HttpURI uri, Stream.Listener listener) throws Exception {
        HttpFields requestFields = new HttpFields();
        requestFields.put(USER_AGENT, USER_AGENT_VERSION);
        MetaData.Request request = new MetaData.Request("GET", uri, HttpVersion.HTTP_2, requestFields);
        HeadersFrame headersFrame = new HeadersFrame(request, null, false);
        FuturePromise<Stream> streamPromise = new FuturePromise<>();
        session.newStream(headersFrame, streamPromise, listener);
        return streamPromise.get();
    }

    @Override
    public void close() throws Exception {
        client.stop();
    }

}
