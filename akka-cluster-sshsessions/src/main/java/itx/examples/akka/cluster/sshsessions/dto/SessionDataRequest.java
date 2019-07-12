package itx.examples.akka.cluster.sshsessions.dto;

import java.io.Serializable;

/**
 * Created by juraj on 3/18/17.
 */
public class SessionDataRequest implements Serializable {

    private final String data;

    public SessionDataRequest(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

}
