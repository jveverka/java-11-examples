package itx.hazelcast.cluster.server.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Cluster;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import com.hazelcast.core.Member;
import itx.hazelcast.cluster.dto.Address;
import itx.hazelcast.cluster.dto.InstanceInfo;
import itx.hazelcast.cluster.server.hazelcast.serializers.InstanceInfoSerializer;
import itx.hazelcast.cluster.server.services.MessageService;
import itx.hazelcast.cluster.server.services.MessageServiceImpl;
import itx.hazelcast.cluster.server.services.RequestRouter;
import itx.hazelcast.cluster.server.websocket.WsServlet;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class ServerApp {

    final private static Logger LOG = LoggerFactory.getLogger(ServerApp.class);

    private HazelcastInstance hazelcastInstance;
    private Server server;
    private ExecutorService executorService;
    private int expectedClusterSize;

    public ServerApp(int expectedClusterSize) {
        this.expectedClusterSize = expectedClusterSize;
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
        MembershipListenerImpl membershipListener = new MembershipListenerImpl(expectedClusterSize);
        // listen on cluster events
        Cluster cluster = hazelcastInstance.getCluster();
        cluster.addMembershipListener(membershipListener);
        LOG.info("Waiting for expected cluster members to join ...");
        membershipListener.awaitClusterFormation(20, TimeUnit.SECONDS);

        // populate clusterInfo map
        IAtomicLong webPortCounter = hazelcastInstance.getAtomicLong("webPortCounter");
        Map<String, InstanceInfo> clusterInfo = hazelcastInstance.getMap( "clusterInfo" );
        InstanceInfo instanceInfo = createInstanceInfo(cluster.getLocalMember(), clusterInfo, (int)webPortCounter.getAndAdd(1));
        clusterInfo.put(instanceInfo.getId(), instanceInfo);
        // register leadership listener
        GateKeepingListener gateKeepingListener = new GateKeepingListenerImpl();
        GateKeeperRunnable gateKeeperRunnable = new GateKeeperRunnable(executorService, hazelcastInstance, gateKeepingListener);
        executorService.submit(gateKeeperRunnable);

        LOG.info("initializing service layer ...");
        MessageService messageService = new MessageServiceImpl(hazelcastInstance);
        RequestRouter requestRouter = new RequestRouter(messageService);

        LOG.info("starting web layer ...");
        server = new Server();
        ServletContextHandler context = new ServletContextHandler(server, "/data", ServletContextHandler.SESSIONS);
        // Register websocket handlers
        ServletHolder webSocketHolder = new ServletHolder(new WsServlet(requestRouter));
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

    private InstanceInfo createInstanceInfo(Member member, Map<String, InstanceInfo> clusterinfo, int webPortOrdinal) throws UnknownHostException {
        LOG.info("clusterinfo size {}", clusterinfo.size());
        int webServerPort = 8080 + webPortOrdinal; //let's start with 8080 and check if this port is free

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
