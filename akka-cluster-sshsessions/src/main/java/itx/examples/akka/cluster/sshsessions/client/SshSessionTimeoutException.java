package itx.examples.akka.cluster.sshsessions.client;

/**
 * Created by juraj on 3/19/17.
 */
public class SshSessionTimeoutException extends Exception {

    public SshSessionTimeoutException() {
        super();
    }

    public SshSessionTimeoutException(String message) {
        super(message);
    }

}
