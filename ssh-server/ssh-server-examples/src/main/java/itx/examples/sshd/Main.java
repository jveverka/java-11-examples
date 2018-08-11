package itx.examples.sshd;

import itx.examples.sshd.commands.StringCommandProcessorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
