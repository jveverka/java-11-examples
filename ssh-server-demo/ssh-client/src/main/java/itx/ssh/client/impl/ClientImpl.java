package itx.ssh.client.impl;

import itx.ssh.client.Client;
import itx.ssh.client.SshSession;
import itx.ssh.client.SshSessionListener;
import itx.ssh.server.commands.keymaps.KeyMap;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelSubsystem;
import org.apache.sshd.client.future.OpenFuture;
import org.apache.sshd.client.keyverifier.ServerKeyVerifier;
import org.apache.sshd.client.session.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ClientImpl implements Client {

    final private static Logger LOG = LoggerFactory.getLogger(ClientImpl.class);

    private final String hostName;
    private final String userName;
    private final int port;
    private final String password;
    private SshClient client;
    private KeyMap keyMap;
    private ServerKeyVerifier serverKeyVerifier;

    public ClientImpl(String hostName, int port, String userName, String password,
                      KeyMap keyMap, ServerKeyVerifier serverKeyVerifier) {
        this.hostName = hostName;
        this.userName = userName;
        this.port = port;
        this.password = password;
        this.keyMap = keyMap;
        this.serverKeyVerifier = serverKeyVerifier;
    }

    @Override
    public void start() {
        this.client = SshClient.setUpDefaultClient();
        if (serverKeyVerifier != null) {
            client.setServerKeyVerifier(serverKeyVerifier);
        }
        this.client.start();
    }

    @Override
    public SshSession getSession(SshSessionListener listener) throws IOException {
        ClientSession session = client.connect(userName, hostName, port)
                .verify().getSession();
        session.addPasswordIdentity(password);
        session.auth().verify(15, TimeUnit.SECONDS);

        while(!session.isOpen()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                LOG.error("Error: ", e);
            }
        }

        ChannelSubsystem robotChannel = session.createSubsystemChannel("ssh-client");

        OpenFuture openFuture = robotChannel.open();

        while(!openFuture.isOpened()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                LOG.error("Error: ", e);
            }
        }

        SshSessionImpl sshSession = new SshSessionImpl(session, robotChannel, listener, keyMap);
        sshSession.init();
        return sshSession;
    }

    @Override
    public void close() throws Exception {
        this.client.stop();
    }

}
