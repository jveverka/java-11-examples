package itx.examples.sshd.tests;

import itx.ssh.server.utils.Utils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

public class TestKeyStore {

    @Test
    public void testLeyStoreLoadKeyPair()
            throws UnrecoverableEntryException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/test-server-keystore.jks");
        KeyPair keyPair = Utils.loadKeyPair(resourceAsStream, "secret", "serverkey", "secret");
        Assert.assertNotNull(keyPair);
    }

    @Test
    public void testLeyStoreLoadPublicKey()
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/test-client-keystore.jks");
        PublicKey publicKey = Utils.loadPublicKey(resourceAsStream, "secret", "serverkey");
        Assert.assertNotNull(publicKey);
    }

}
