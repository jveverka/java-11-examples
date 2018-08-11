package itx.ssh.client;

import itx.ssh.client.impl.ClientImpl;

public class ClientBuilder {

    private String hostName;
    private String userName;
    private int port;
    private String password;

    public ClientBuilder() {
    }

    public ClientBuilder setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public ClientBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public ClientBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public ClientBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public Client build() {
        return new ClientImpl(hostName, port, userName, password);
    }

}
