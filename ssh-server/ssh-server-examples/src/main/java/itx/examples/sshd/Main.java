package itx.examples.sshd;

import itx.examples.sshd.commands.CommandProcessorImpl;
import itx.ssh.server.SshServerBuilder;
import itx.ssh.server.auth.KeyPairProviderBuilder;
import itx.ssh.server.auth.PasswordAuthenticatorBuilder;
import itx.ssh.server.commands.CommandProcessor;
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

        ServerApp app = new ServerApp();
        app.startApplication();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    LOG.info("shutting down ssh server ");
                    app.stop();
                    countDownLatch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        countDownLatch.await();
    }


}
