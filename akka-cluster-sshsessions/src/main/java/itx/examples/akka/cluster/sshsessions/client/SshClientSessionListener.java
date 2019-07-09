package itx.examples.akka.cluster.sshsessions.client;

/**
 * Created by juraj on 3/19/17.
 */
public interface SshClientSessionListener {

    public void onData(String data);

    public void onError(String reason);

    public void onClose();

}
