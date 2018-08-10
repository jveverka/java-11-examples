package itx.examples.sshd;

import itx.ssh.server.auth.KeyPairProviderBuilder;
import itx.ssh.server.auth.PasswordAuthenticatorBuilder;
import itx.ssh.server.commands.CommandFactoryImpl;
import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.CommandProcessorImpl;
import itx.ssh.server.commands.ShellFactoryImpl;
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
import java.util.concurrent.CountDownLatch;

public class Main {

    final private static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)
            throws IOException, InterruptedException, UnrecoverableKeyException, CertificateException,
            NoSuchAlgorithmException, KeyStoreException {
        LOG.info("starting ssh server ");

        CountDownLatch countDownLatch = new CountDownLatch(1);
        int port = 2222;
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

        CommandProcessor commandProcessor = new CommandProcessorImpl();
        KeyMapProvider keyMapProvider = new KeyMapProvider();
        KeyMap keyMap = keyMapProvider.getDefaultKeyMap();

        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);
        sshd.setPasswordAuthenticator(passwordAuthenticator);
        sshd.setKeyPairProvider(keyPairProvider);
        sshd.setShellFactory(new ShellFactoryImpl("CMD: ", keyMap, commandProcessor));
        sshd.setCommandFactory(new CommandFactoryImpl(commandProcessor));
        sshd.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    LOG.info("shutting down ssh server ");
                    sshd.stop();
                    countDownLatch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        LOG.info("Listening on port {}", port);
        countDownLatch.await();
    }


}
