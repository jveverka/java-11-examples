package itx.examples.jce.tests;

import itx.examples.jce.JCEUtils;
import itx.examples.jce.KeyPairHolder;
import org.bouncycastle.operator.OperatorCreationException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JCETests {

    private static KeyPair CAKeyPair;
    private static X509Certificate CACertificate;
    private static Map<String, KeyPairHolder> keyPairs = new HashMap<>();

    @Test
    @Order(1)
    public void testGenerateKeyPair() throws NoSuchAlgorithmException {
        CAKeyPair = JCEUtils.generateKeyPair();
        assertNotNull(CAKeyPair);
    }

    @Test
    @Order(2)
    public void testGenerateSelfSignedX509CACertificate() throws CertificateException, OperatorCreationException, IOException {
        CACertificate = JCEUtils.createSelfSignedCertificate("ca-subject", System.currentTimeMillis(), 3600*24*365L, CAKeyPair);
        assertNotNull(CACertificate);
    }

    @Test
    @Order(3)
    public void testGenerateClientKeypairAndSignedX509Certificate() throws NoSuchAlgorithmException, CertificateException, OperatorCreationException, IOException {
        KeyPair clientKeyPair = JCEUtils.generateKeyPair();
        X509Certificate clientCertificate = JCEUtils.createSignedCertificate(CACertificate.getIssuerDN().getName(), "client-01", System.currentTimeMillis(), 3600*24*365L, clientKeyPair.getPublic(), CAKeyPair.getPrivate());
        keyPairs.put("client-01", new KeyPairHolder(clientKeyPair, clientCertificate));
        assertNotNull(clientKeyPair);
        assertNotNull(clientCertificate);
    }

    @Test
    @Order(4)
    public void verifyClientCertificateValidity() throws CertificateException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        KeyPairHolder keyPairHolder = keyPairs.get("client-01");
        X509Certificate clientCertificate = keyPairHolder.getCACertificate();
        int version = clientCertificate.getVersion();
        assertTrue(version == 3);
        JCEUtils.verifyCertificate(clientCertificate, CACertificate);
    }

    @Test
    @Order(5)
    public void testDigitalSignature() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] data = "Data String".getBytes();
        KeyPairHolder keyPairHolder = keyPairs.get("client-01");
        byte[] digitalSignature = JCEUtils.createDigitalSignature(data, keyPairHolder.getCAKeyPair().getPrivate());
        boolean valid = JCEUtils.verifyDigitalSignature(data, digitalSignature, keyPairHolder.getCACertificate());
        assertTrue(valid);
        valid = JCEUtils.verifyDigitalSignature("Other Data".getBytes(), digitalSignature, keyPairHolder.getCACertificate());
        assertFalse(valid);
    }

    @Test
    @Order(6)
    public void testEnctyptAndDecryptData() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        String dataString = "Data String";
        KeyPairHolder keyPairHolder = keyPairs.get("client-01");
        byte[] encryptedData = JCEUtils.encrypt(dataString.getBytes(StandardCharsets.UTF_8), keyPairHolder.getCAKeyPair().getPrivate());
        byte[] decryptedData = JCEUtils.decrypt(encryptedData, keyPairHolder.getCACertificate().getPublicKey());
        String decryptedString = new String(decryptedData, StandardCharsets.UTF_8);
        assertEquals(dataString, decryptedString);
    }

}
