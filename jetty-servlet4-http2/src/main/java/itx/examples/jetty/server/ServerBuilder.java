package itx.examples.jetty.server;

import itx.examples.jetty.server.http2.ServerConnectionFactory;
import itx.examples.jetty.server.http2.StreamProcessorFactory;
import itx.examples.jetty.server.http2.StreamProcessorRegistration;
import org.eclipse.jetty.alpn.ALPN;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.DispatcherType;
import java.security.KeyStore;
import java.util.*;

public class ServerBuilder {

    private Map<String, ServletHolder> servletHandlers;
    private Map<String, StreamProcessorRegistration> streamProcessors;
    private Map<String, FilterHolder> filters;
    private List<EventListener> sessionEventListeners;
    private ResourceConfig resourceConfig;
    private int secureHttpPort = 8443;
    private int httpPort = 8080;
    private KeyStore keyStore;
    private String keyStorePassword;
    private String contextUrn = "/";
    private String restUriPrefix = "/rest/*";
    private String staticResourceBasePath = "/web";
    private String staticResourceBaseUrn = "/static/*";

    public ServerBuilder() {
        this.servletHandlers = new HashMap<>();
        this.streamProcessors = new HashMap<>();
        this.filters = new HashMap<>();
        this.sessionEventListeners = new ArrayList<>();
    }

    public ServerBuilder setContextUrn(String contextUrn) {
        this.contextUrn = contextUrn;
        return this;
    }

    public ServerBuilder addSessionEventListener(EventListener eventListener) {
        sessionEventListeners.add(eventListener);
        return this;
    }

    public ServerBuilder addServletFilter(String urn, FilterHolder filterHolder) {
        filters.put(urn, filterHolder);
        return this;
    }

    public ServerBuilder setStaticResourceBasePath(String staticResourceBasePath) {
        this.staticResourceBasePath = staticResourceBasePath;
        return this;
    }

    public ServerBuilder setStaticResourceBaseUrn(String staticResourceBaseUrn) {
        this.staticResourceBaseUrn = staticResourceBaseUrn;
        return this;
    }

    public ServerBuilder setRestUriPrefix(String restUriPrefix) {
        this.restUriPrefix = restUriPrefix;
        return this;
    }

    public ServerBuilder setResourceConfig(ResourceConfig resourceConfig) {
        this.resourceConfig = resourceConfig;
        return this;
    }

    public ServerBuilder addStreamProcessorFactory(String urn, StreamProcessorFactory factory) {
        streamProcessors.put(urn, new StreamProcessorRegistration(urn, factory));
        return this;
    }

    public ServerBuilder addServletHolder(String urn, ServletHolder servletHolder) {
        servletHandlers.put(urn, servletHolder);
        return this;
    }

    public ServerBuilder setSecureHttpPort(int secureHttpPort) {
        this.secureHttpPort = secureHttpPort;
        return this;
    }

    public ServerBuilder setHttpPort(int httpPort) {
        this.httpPort = httpPort;
        return this;
    }

    public ServerBuilder setKeyStore(KeyStore keyStore) {
        this.keyStore = keyStore;
        return this;
    }

    public ServerBuilder setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
        return this;
    }

    public Server build() throws Exception {
        Server server = new Server();

        // Register servlets
        ServletContextHandler context = new ServletContextHandler(server, contextUrn, ServletContextHandler.SESSIONS);
        servletHandlers.forEach((uri, servletHolder) -> { context.addServlet(servletHolder, uri);});
        // Register servlet filters
        filters.forEach((urn, filterHolder) -> { context.addFilter(filterHolder, urn,
                EnumSet.of(DispatcherType.REQUEST)); });
        // Register EventListener instances
        sessionEventListeners.forEach( listener -> { context.getSessionHandler().addEventListener(listener); });

        // Register jersey rest services
        ServletContainer restServletContainer = new ServletContainer(resourceConfig);
        ServletHolder restServletHolder = new ServletHolder(restServletContainer);
        context.addServlet(restServletHolder, restUriPrefix);

        // Register static resources (html pages, images, javascripts, ...)
        String externalResource = this.getClass().getResource(staticResourceBasePath).toExternalForm();
        DefaultServlet defaultServlet = new DefaultServlet();
        ServletHolder holderPwd = new ServletHolder("default", defaultServlet);
        holderPwd.setInitParameter("resourceBase", externalResource);
        context.addServlet(holderPwd, staticResourceBaseUrn);

        server.setHandler(context);

        // HTTP Configuration
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSecureScheme("https");
        httpConfig.setSecurePort(secureHttpPort);
        httpConfig.setSendXPoweredBy(true);
        httpConfig.setSendServerVersion(true);

        // HTTP Connector
        HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConfig);
        HTTP2CServerConnectionFactory http2CServerConnectionFactory = new HTTP2CServerConnectionFactory(httpConfig);
        ServerConnector http = new ServerConnector(server, httpConnectionFactory, http2CServerConnectionFactory);
        http.setPort(httpPort);
        server.addConnector(http);

        // SSL Context Factory for HTTPS and HTTP/2
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setTrustStore(keyStore);
        sslContextFactory.setTrustStorePassword(keyStorePassword);
        sslContextFactory.setKeyStore(keyStore);
        sslContextFactory.setKeyStorePassword(keyStorePassword);
        sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);

        // HTTPS Configuration
        HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());

        // HTTP/2 Connection Factory
        ServerConnectionFactory h2 = new ServerConnectionFactory(httpsConfig, streamProcessors);

        //NegotiatingServerConnectionFactory.checkProtocolNegotiationAvailable();
        ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
        alpn.setDefaultProtocol(http.getDefaultProtocol());

        // SSL Connection Factory
        SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());

        // HTTP/2 Connector
        ServerConnector http2Connector =
                new ServerConnector(server, ssl, alpn, h2, new HttpConnectionFactory(httpsConfig));
        http2Connector.setPort(secureHttpPort);
        server.addConnector(http2Connector);

        ALPN.debug=false;

        return server;
    }

}
