package itx.hazelcast.cluster.server.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ISemaphore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

public class GateKeeperRunnable implements Runnable {

    final private static Logger LOG = LoggerFactory.getLogger(GateKeeperRunnable.class);

    private HazelcastInstance hazelcastInstance;
    private GateKeepingListener listener;
    private Executor executor;

    public GateKeeperRunnable(Executor executor, HazelcastInstance hazelcastInstance, GateKeepingListener listener) {
        this.executor = executor;
        this.hazelcastInstance = hazelcastInstance;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            LOG.info("acquiring gatekeeper lock");
            ISemaphore semaphore = hazelcastInstance.getSemaphore("gatekeeper");
            semaphore.init(1);
            semaphore.acquire();
            listener.onGateKeepingEvent();
        } catch (InterruptedException e) {
            executor.execute(this);
        }
    }

}
