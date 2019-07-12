package itx.examples.akka.cluster.sshsessions.client;

import java.io.Serializable;

/**
 * Created by juraj on 25.3.2017.
 */
public class UserCredentials implements Serializable {

    private final String userName;
    private final String password;

    public UserCredentials(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

}
