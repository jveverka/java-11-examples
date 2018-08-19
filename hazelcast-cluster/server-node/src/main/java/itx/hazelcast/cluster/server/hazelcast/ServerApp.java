package itx.hazelcast.cluster.server.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Cluster;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ISemaphore;
import com.hazelcast.core.Member;
import com.hazelcast.core.MembershipListener;
import itx.hazelcast.cluster.dto.Address;
import itx.hazelcast.cluster.dto.InstanceInfo;
import itx.hazelcast.cluster.server.hazelcast.serializers.InstanceInfoSerializer;
import itx.hazelcast.cluster.server.rest.RestApplication;
import itx.hazelcast.cluster.server.services.DataService;
import itx.hazelcast.cluster.server.services.DataServiceImpl;
import itx.hazelcast.cluster.server.websocket.WsServlet;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerApp {

    final private static Logger LOG = LoggerFactory.getLogger(ServerApp.class);

    private HazelcastInstance hazelcastInstance;
    private Server server;
    private ExecutorService executorService;

    public ServerApp() {
    }

    public void startServer() throws Exception {
        LOG.info("starting hazelcast ...");
        executorService = Executors.newSingleThreadExecutor();
        // Register serializers in hazelcast configuration
        SerializerConfig sc = new SerializerConfig()
                .setImplementation(new InstanceInfoSerializer())
                .setTypeClass(InstanceInfo.class);
        Config config = new Config();
        config.getSerializationConfig().addSerializerConfig(sc);
        // Create hazelcast instance
        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        MembershipListener membershipListener = new MembershipListenerImpl();
        // listen on cluster events
        Cluster cluster = hazelcastInstance.getCluster();
        cluster.addMembershipListener(membershipListener);
        // populate clusterInfo map
        Map<String, InstanceInfo> clusterInfo = hazelcastInstance.getMap( "clusterInfo" );
        InstanceInfo instanceInfo = createInstanceInfo(cluster.getLocalMember(), clusterInfo);
        clusterInfo.put(instanceInfo.getId(), instanceInfo);
        // register leadership listener
        GateKeepingListener gateKeepingListener = new GateKeepingListenerImpl();
        GateKeeperRunnable gateKeeperRunnable = new GateKeeperRunnable(executorService, hazelcastInstance, gateKeepingListener);
        executorService.submit(gateKeeperRunnable);

        LOG.info("initializing service layer ...");
        DataService dataService = new DataServiceImpl(hazelcastInstance);

        LOG.info("starting web layer ...");
        server = new Server();
        ServletContextHandler context = new ServletContextHandler(server, "/data", ServletContextHandler.SESSIONS);
        // Register jersey rest services
        RestApplication resourceConfig = new RestApplication(dataService);
        ServletContainer restServletContainer = new ServletContainer(resourceConfig);
        ServletHolder restServletHolder = new ServletHolder(restServletContainer);
        context.addServlet(restServletHolder, "/rest");
        // Register websocket handlers
        ServletHolder webSocketHolder = new ServletHolder(new WsServlet());
        context.addServlet(webSocketHolder, "/websocket");
        // Setup http connectors
        HttpConfiguration httpConfig = new HttpConfiguration();
        HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConfig);
        ServerConnector http = new ServerConnector(server, httpConnectionFactory);
        http.setPort(instanceInfo.getWebServerPort());
        server.addConnector(http);
        server.start();
        LOG.info("init sequence done.");
    }

    public void stopServer() throws Exception {
        LOG.info("stop.");
        if (server != null) {
            server.stop();
        }
        if (hazelcastInstance != null) {
            //ISemaphore semaphore = hazelcastInstance.getSemaphore("gatekeeper");
            //semaphore.release();
            //hazelcastInstance.shutdown();
        }
        executorService.shutdown();
    }

    private InstanceInfo createInstanceInfo(Member member, Map<String, InstanceInfo> clusterinfo) throws UnknownHostException {
        LOG.info("clusterinfo size {}", clusterinfo.size());
        int webServerPort = 8080; //let's start with 8080 and check if this port is free
        InetSocketAddress inetSocketAddress = member.getAddress().getInetSocketAddress();
        for (InstanceInfo i: clusterinfo.values()) {
            if (inetSocketAddress.getHostName().equals(i.getAddress().getHostName())) {
                if (webServerPort == i.getWebServerPort()) {
                    webServerPort = i.getWebServerPort() + 1;
                }
            }
        }

        Address address = Address.newBuilder()
                .setHostName(member.getAddress().getInetSocketAddress().getHostName())
                .setPort(member.getAddress().getInetSocketAddress().getPort())
                .build();
        InstanceInfo instanceInfo = InstanceInfo.newBuilder()
                .setId(member.getUuid())
                .setAddress(address)
                .setWebServerPort(webServerPort)
                .build();

        LOG.info("webServerPort: {}", webServerPort);
        return instanceInfo;
    }

}
