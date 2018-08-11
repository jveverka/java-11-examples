package itx.ssh.client.impl;

import itx.ssh.client.Client;
import itx.ssh.client.SshSession;
import itx.ssh.client.SshSessionListener;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ClientImpl implements Client {

    private final String hostName;
    private final String userName;
    private final int port;
    private final String password;
    private SshClient client;

    public ClientImpl(String hostName, int port, String userName, String password) {
        this.hostName = hostName;
        this.userName = userName;
        this.port = port;
        this.password = password;
    }

    @Override
    public void start() {
        this.client = SshClient.setUpDefaultClient();
        this.client.start();
    }

    @Override
    public SshSession getSession(SshSessionListener listener) throws IOException {
        ClientSession session = client.connect(userName, hostName, port)
                .verify().getSession();
        session.addPasswordIdentity(password);
        session.auth().verify(15, TimeUnit.SECONDS);
        ClientChannel channel = session.createChannel(ClientChannel.CHANNEL_SHELL);
        channel.open();
        return new SshSessionImpl(session, channel, listener);
    }

    @Override
    public void close() throws Exception {
        this.client.stop();
    }

}
