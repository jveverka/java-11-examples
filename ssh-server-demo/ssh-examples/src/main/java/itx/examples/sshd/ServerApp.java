package itx.examples.sshd;

import itx.examples.sshd.commands.SshClientCommandProcessor;
import itx.examples.sshd.commands.StringCommandProcessorImpl;
import itx.examples.sshd.sessions.SshClientSessionListenerImpl;
import itx.ssh.server.SshServerBuilder;
import itx.ssh.server.auth.KeyPairProviderBuilder;
import itx.ssh.server.auth.PasswordAuthenticatorBuilder;
import itx.ssh.server.commands.keymaps.KeyMap;
import itx.ssh.server.commands.keymaps.KeyMapProvider;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class ServerApp {

    final private static Logger LOG = LoggerFactory.getLogger(ServerApp.class);

    private SshServer sshd;
    private SshClientCommandProcessor sshClientCommandProcessor;
    private StringCommandProcessorImpl stringCommandProcessor;

    public ServerApp() {
    }

    public void startApplication() throws IOException, UnrecoverableKeyException, CertificateException,
            NoSuchAlgorithmException, KeyStoreException {
        LOG.info("starting ssh server ");

        int port = 2222;
        String prompt = "CMD: ";
        SshClientSessionListenerImpl sshClientSessionListener = new SshClientSessionListenerImpl();
        stringCommandProcessor = new StringCommandProcessorImpl();
        sshClientCommandProcessor = new SshClientCommandProcessor(sshClientSessionListener);

        PasswordAuthenticator passwordAuthenticator = new PasswordAuthenticatorBuilder()
                .addCredentials("user", "secret")
                .build();

        InputStream resourceAsStream = Main.class.getClassLoader().getResourceAsStream("server-keystore.jks");
        KeyPairProvider keyPairProvider = new KeyPairProviderBuilder()
                .setIs(resourceAsStream)
                .setKeyPairAlias("serverkey")
                .setKeystorePassword("secret")
                .setKeyPairPassword("secret")
                .build();

        KeyMap keyMap = KeyMapProvider.createDefaultKeyMap();

        sshd = new SshServerBuilder()
                .setPort(port)
                .withKeyMap(keyMap)
                .withKeyPairProvider(keyPairProvider)
                .withPasswordAuthenticator(passwordAuthenticator)
                .withCommandFactory(stringCommandProcessor)
                .withShellFactory(prompt, stringCommandProcessor)
                .withSshClientProcessor(sshClientCommandProcessor, sshClientSessionListener)
                .build();
        sshd.start();
        LOG.info("Listening on port {}", port);
    }

    public void stop() throws Exception {
        if (sshClientCommandProcessor != null) {
            sshClientCommandProcessor.close();
        }
        if (stringCommandProcessor != null) {
            stringCommandProcessor.close();
        }
        if (sshd != null) {
            LOG.info("Shutting down.");
            sshd.stop();
        }
    }

}
