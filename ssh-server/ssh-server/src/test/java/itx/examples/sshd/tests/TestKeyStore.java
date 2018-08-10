package itx.examples.sshd.tests;

import itx.ssh.server.utils.Utils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

public class TestKeyStore {

    @Test
    public void testLeyStoreLoad()
            throws UnrecoverableEntryException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/server-keystore.jks");
        KeyPair keyPair = Utils.loadKeyStore(resourceAsStream, "secret", "serverkey", "secret");
        Assert.assertNotNull(keyPair);
    }

}
