package itx.examples.akka.cluster.sshsessions.client;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Created by juraj on 3/19/17.
 */
public interface SshClientSession extends AutoCloseable {

    public String getId();

    public void sendData(String data);

}
