package itx.hazelcast.cluster.server;

import itx.hazelcast.cluster.server.hazelcast.ServerApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    final private static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        LOG.info("hazelcast cluster server started ...");
        ServerApp serverApp = new ServerApp();
        serverApp.startServer();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    serverApp.stopServer();
                } catch (Exception e) {
                    LOG.error("Error:", e);
                }
            }
        });
    }

}
