package itx.examples.jce.tests;

import itx.examples.jce.JCEUtils;
import itx.examples.jce.KeyPairHolder;
import itx.examples.jce.PKIException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JCETests {

    private static KeyPair CAKeyPair;
    private static X509Certificate CACertificate;
    private static Map<String, KeyPairHolder> keyPairs = new HashMap<>();
    private static KeyPairHolder keyPairHolder;

    @BeforeAll
    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    @Order(1)
    public void testGenerateKeyPair() throws PKIException {
        CAKeyPair = JCEUtils.generateKeyPair();
        assertNotNull(CAKeyPair);
    }

    @Test
    @Order(2)
    public void testGenerateSelfSignedX509CACertificate() throws PKIException {
        CACertificate = JCEUtils.createSelfSignedCertificate("ca-subject", new Date(), 1L, TimeUnit.HOURS, CAKeyPair);
        assertNotNull(CACertificate);
    }

    @Test
    @Order(3)
    public void testGenerateClientKeypairAndSignedX509Certificate() throws PKIException {
        KeyPair clientKeyPair = JCEUtils.generateKeyPair();
        X509Certificate clientCertificate = JCEUtils.createSignedCertificate(CACertificate.getIssuerDN().getName(),
                "client-01", new Date(), 1L, TimeUnit.HOURS, clientKeyPair.getPublic(), CAKeyPair.getPrivate());
        keyPairs.put("client-01", new KeyPairHolder(clientKeyPair.getPrivate(), clientCertificate));
        assertNotNull(clientKeyPair);
        assertNotNull(clientCertificate);
    }

    @Test
    @Order(4)
    public void verifyClientCertificateValidity() throws PKIException {
        KeyPairHolder keyPairHolder = keyPairs.get("client-01");
        X509Certificate clientCertificate = keyPairHolder.getCertificate();
        int version = clientCertificate.getVersion();
        assertTrue(version == 3);
        boolean result = JCEUtils.verifySignedCertificate(CACertificate, clientCertificate);
        assertTrue(result);
    }

    @Test
    @Order(5)
    public void verifyCACertificateValidity() throws PKIException {
        boolean result = JCEUtils.verifySelfSignedCertificate(CACertificate);
        assertTrue(result);
    }

    @Test
    @Order(6)
    public void testDigitalSignature() throws PKIException {
        byte[] data = "Data String".getBytes();
        KeyPairHolder keyPairHolder = keyPairs.get("client-01");
        byte[] digitalSignature = JCEUtils.createDigitalSignature(data, keyPairHolder.getKeyPair().getPrivate());
        boolean valid = JCEUtils.verifyDigitalSignature(data, digitalSignature, keyPairHolder.getCertificate());
        assertTrue(valid);
        valid = JCEUtils.verifyDigitalSignature("Other Data".getBytes(), digitalSignature, keyPairHolder.getCertificate());
        assertFalse(valid);
    }

    @Test
    @Order(7)
    public void testEncryptAndDecryptDataPrivatePublic() throws PKIException {
        String dataString = "Data String";
        KeyPairHolder keyPairHolder = keyPairs.get("client-01");
        byte[] encryptedData = JCEUtils.encrypt(dataString.getBytes(StandardCharsets.UTF_8), keyPairHolder.getPrivateKey());
        byte[] decryptedData = JCEUtils.decrypt(encryptedData, keyPairHolder.getPublicKey());
        String decryptedString = new String(decryptedData, StandardCharsets.UTF_8);
        assertEquals(dataString, decryptedString);
    }

    @Test
    @Order(8)
    public void testEncryptAndDecryptDataPublicPrivate() throws PKIException {
        String dataString = "Data String";
        KeyPairHolder keyPairHolder = keyPairs.get("client-01");
        byte[] encryptedData = JCEUtils.encrypt(dataString.getBytes(StandardCharsets.UTF_8), keyPairHolder.getPublicKey());
        byte[] decryptedData = JCEUtils.decrypt(encryptedData, keyPairHolder.getPrivateKey());
        String decryptedString = new String(decryptedData, StandardCharsets.UTF_8);
        assertEquals(dataString, decryptedString);
    }

    @Test
    @Order(9)
    public void loadPrivateKeyAndCertificateFromJKS() throws PKIException {
        String keystorePath = "/keystore.jks";
        String alias = "organization";
        String keystorePassword = "secret";
        String privateKeyPassword = "secret";
        keyPairHolder = JCEUtils.loadPrivateKeyAndCertificateFromJKS(keystorePath, alias, keystorePassword, privateKeyPassword);
        assertNotNull(keyPairHolder);
        assertNotNull(keyPairHolder.getCertificate());
        assertNotNull(keyPairHolder.getPrivateKey());
    }

    @Test
    @Order(10)
    public void certificateSerializationAndDeserialization() throws PKIException {
        byte[] encoded = JCEUtils.serializeX509Certificate(keyPairHolder.getCertificate());
        X509Certificate certificate = JCEUtils.deserializeX509Certificate(encoded);
        assertNotNull(certificate);
        assertEquals(keyPairHolder.getCertificate(), certificate);
    }

    @Test
    @Order(11)
    public void privateKeySerializationAndDeserialization() throws PKIException {
        byte[] encoded = JCEUtils.serializePrivateKey(keyPairHolder.getPrivateKey());
        PrivateKey privateKey = JCEUtils.deserializePrivateKey(encoded);
        assertNotNull(privateKey);
        assertEquals(keyPairHolder.getPrivateKey(), privateKey);
    }

    @Test
    @Order(12)
    public void createJKSWithPrivateKeyAndCertificate() throws PKIException {
        KeyPairHolder keyPair = JCEUtils.generateSelfSignedKeyPairHolder("issuerAndSubject", new Date(), 1L, TimeUnit.HOURS);
        byte[] jksBytes = JCEUtils.createJKSWithPrivateKeyAndCertificate("alias", "secret", "secret", keyPair);
        assertNotNull(jksBytes);
        KeyPairHolder loadedKeyPair = JCEUtils.loadKeypairFromJKS("alias", "secret", "secret", jksBytes);
        assertNotNull(loadedKeyPair);
        assertNotNull(loadedKeyPair.getCertificate());
        assertNotNull(loadedKeyPair.getKeyPair());
        assertNotNull(loadedKeyPair.getPrivateKey());
        assertEquals(keyPair, loadedKeyPair);
        assertEquals(keyPair.getCertificate(), loadedKeyPair.getCertificate());
        assertEquals(keyPair.getPrivateKey(), loadedKeyPair.getPrivateKey());
    }

}
