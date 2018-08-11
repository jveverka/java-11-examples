package itx.ssh.client;

import itx.ssh.client.impl.ClientImpl;
import itx.ssh.server.commands.keymaps.KeyMap;
import itx.ssh.server.commands.keymaps.KeyMapProvider;

public class ClientBuilder {

    private String hostName;
    private String userName;
    private int port;
    private String password;
    private KeyMap keyMap;

    public ClientBuilder() {
        this.keyMap = KeyMapProvider.createDefaultKeyMap();
    }

    public ClientBuilder setKeyMap(KeyMap keyMap) {
        this.keyMap = keyMap;
        return this;
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
        return new ClientImpl(hostName, port, userName, password, keyMap);
    }

}
