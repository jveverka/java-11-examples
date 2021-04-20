package itx.hazelcast.cluster.server;

import itx.hazelcast.cluster.server.hazelcast.ServerApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    final private static Logger LOG = LoggerFactory.getLogger(Main.class);
    final private static String[] banner = new String[] {
            "_________ .____       ______________________________",
            "\\_   ___ \\|    |     /   _____/\\__    ___/\\______   \\",
            "/    \\  \\/|    |     \\_____  \\   |    |    |       _/",
            "\\     \\___|    |___  /        \\  |    |    |    |   \\",
            " \\______  /_______ \\/_______  /  |____|    |____|_  /",
            "  \\/        \\/        \\/                    \\/       "
    };

    public static void main(String[] args) throws Exception {
        LOG.info("hazelcast cluster server started ...");
        int expectedClusterSize = getExpectedClusterSize(args);
        LOG.info("expected cluster size: {}", expectedClusterSize);
        for (String s : banner) {
            System.out.println(s);
        }
        ServerApp serverApp = new ServerApp(expectedClusterSize);
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

    private static int getExpectedClusterSize(String[] args) {
        try {
            return Integer.parseInt(args[0]);
        } catch (Exception e) {
            return 1;
        }
    }

}
