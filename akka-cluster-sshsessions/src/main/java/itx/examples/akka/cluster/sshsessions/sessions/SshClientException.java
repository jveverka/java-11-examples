package itx.examples.akka.cluster.sshsessions.sessions;

/**
 * Created by juraj on 25.3.2017.
 */
public class SshClientException extends Exception {

    public SshClientException() {
        super();
    }

    public SshClientException(String message) {
        super(message);
    }

    public SshClientException(Throwable e) {
        super(e);
    }

}
