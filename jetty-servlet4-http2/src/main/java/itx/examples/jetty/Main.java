package itx.examples.jetty;

import itx.examples.jetty.common.SystemUtils;
import itx.examples.jetty.common.services.EchoService;
import itx.examples.jetty.common.services.MessageServiceAsync;
import itx.examples.jetty.common.services.SystemInfoService;
import itx.examples.jetty.server.ServerBuilder;
import itx.examples.jetty.server.rest.RestApplication;
import itx.examples.jetty.server.services.EchoServiceImpl;
import itx.examples.jetty.server.services.MessageServiceImpl;
import itx.examples.jetty.server.services.SystemInfoServiceImpl;
import itx.examples.jetty.server.servlet.*;
import itx.examples.jetty.server.streams.StreamEchoProcessorFactory;
import itx.examples.jetty.server.streams.StreamMessageProcessorFactory;
import itx.examples.jetty.server.ws.WsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyStore;

public class Main {

    final private static Logger LOG = LoggerFactory.getLogger(Main.class);

    private static Server server;

    public static void main(String[] args) {
        try {
            LOG.info("Jetty server starting ...");
            String password = "secret";
            KeyStore keyStore = SystemUtils.loadJKSKeyStore("server.jks", password);
            String baseUri;
            ServerBuilder serverBuilder = new ServerBuilder();
            serverBuilder.setContextUrn("/");
            MessageServiceAsync messageService = new MessageServiceImpl();
            SystemInfoService systemInfoService = new SystemInfoServiceImpl();
            EchoService echoService = new EchoServiceImpl();

            baseUri = "/data/sync";
            ServletHolder servletHolderDataSync = new ServletHolder(new DataSyncServlet(baseUri, messageService));
            serverBuilder.addServletHolder(baseUri + "/*", servletHolderDataSync);

            baseUri = "/data/async";
            ServletHolder servletHolderDataAsync = new ServletHolder(new DataAsyncServlet(baseUri, messageService));
            serverBuilder.addServletHolder(baseUri + "/*", servletHolderDataAsync);

            baseUri = "/data/echo";
            ServletHolder servletHolderEcho = new ServletHolder(new EchoServiceServlet(baseUri, echoService));
            serverBuilder.addServletHolder(baseUri + "/*", servletHolderEcho);

            baseUri = "/data/system/info";
            ServletHolder servletHolderSystemInfo = new ServletHolder(new SystemInfoServlet(baseUri, systemInfoService));
            serverBuilder.addServletHolder(baseUri + "/*", servletHolderSystemInfo);

            baseUri = "/websocket";
            ServletHolder webSocketHolder = new ServletHolder(new WsServlet());
            serverBuilder.addServletHolder(baseUri, webSocketHolder);

            baseUri = "/data";
            FilterHolder filterHolder = new FilterHolder(new CustomFilter());
            serverBuilder.addServletFilter(baseUri + "/*", filterHolder);

            serverBuilder.addStreamProcessorFactory("/stream/echo", new StreamEchoProcessorFactory(echoService));
            serverBuilder.addStreamProcessorFactory("/stream/messages", new StreamMessageProcessorFactory(messageService));

            serverBuilder.setRestUriPrefix("/rest/*");
            serverBuilder.setResourceConfig(new RestApplication(systemInfoService));

            serverBuilder.setStaticResourceBasePath("/web");
            serverBuilder.setStaticResourceBaseUrn("/static/*");

            serverBuilder.addSessionEventListener(new CustomHttpSessionListener());
            serverBuilder.addSessionEventListener(new CustomHttpSessionIdListener());

            serverBuilder.setKeyStore(keyStore);
            serverBuilder.setSecureHttpPort(8443);
            serverBuilder.setHttpPort(8080);
            serverBuilder.setKeyStorePassword(password);

            server = serverBuilder.build();
            server.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        server.stop();
                        LOG.info("stopped.");
                    } catch (Exception e) {
                        LOG.error("ERROR: ", e);
                    }
                }
            });
            server.join();
        } catch (Exception e) {
            LOG.error("ERROR: ", e);
        }
    }

}
