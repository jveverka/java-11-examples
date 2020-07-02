package itx.examples.jce;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class JCEUtils {

    private JCEUtils() {
        throw new UnsupportedOperationException();
    }

    private static final String BC_PROVIDER = "BC";
    private static final String SHA256_RSA = "SHA256withRSA";
    private static final String CN_NAME = "CN=";
    private static final String X509 = "X.509";
    private static final String TRANSFORMATION = "RSA/None/OAEPWITHSHA-256ANDMGF1PADDING";
    private static final String KEYSTORE_TYPE = "JKS";
    private static final String ALGORITHM = "RSA";
    private static final String RANDOM_ALGORITHM = "NativePRNG";

    public static KeyPair generateKeyPair() throws PKIException {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, BC_PROVIDER);
            SecureRandom secureRandom = SecureRandom.getInstance(RANDOM_ALGORITHM);
            keyPairGenerator.initialize(2048, secureRandom);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new PKIException(e);
        }
    }

    public static KeyPairHolder generateSelfSignedKeyPairHolder(String issuerAndSubject, Date notBefore, Long duration, TimeUnit timeUnit) throws PKIException {
        KeyPair keyPair = generateKeyPair();
        X509Certificate x509Certificate = createSelfSignedCertificate(issuerAndSubject, notBefore, duration, timeUnit, keyPair);
        return new KeyPairHolder(keyPair.getPrivate(), x509Certificate);
    }

    public static X509Certificate createSignedCertificate(String issuerName, String subjectName, Date notBefore, Long duration, TimeUnit timeUnit, PublicKey publicKey, PrivateKey privateKey) throws PKIException {
        try {
            X500Name issuer = new X500Name(CN_NAME + issuerName);
            BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
            Date notAfter = new Date(notBefore.getTime() + timeUnit.toMillis(duration));
            X500Name subject = new X500Name(CN_NAME + subjectName);
            SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
            X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(issuer, serial, notBefore, notAfter, subject, publicKeyInfo);
            JcaContentSignerBuilder jcaContentSignerBuilder = new JcaContentSignerBuilder(SHA256_RSA);
            ContentSigner signer = jcaContentSignerBuilder.build(privateKey);
            CertificateFactory certificateFactory = CertificateFactory.getInstance(X509, BC_PROVIDER);
            byte[] certBytes = certBuilder.build(signer).getEncoded();
            return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certBytes));
        } catch (Exception e) {
            throw new PKIException(e);
        }
    }

    public static X509Certificate createSelfSignedCertificate(String issuerAndSubject, Date notBefore, Long duration, TimeUnit timeUnit, KeyPair keyPair) throws PKIException {
        return createSignedCertificate(issuerAndSubject, issuerAndSubject, notBefore, duration, timeUnit, keyPair.getPublic(), keyPair.getPrivate());
    }

    public static boolean verifySelfSignedCertificate(X509Certificate certificate) {
        return verifySignedCertificate(certificate, certificate);
    }

    public static boolean verifySignedCertificate(X509Certificate issuerCertificate, X509Certificate signedCertificate) {
        try {
            issuerCertificate.checkValidity();
            signedCertificate.checkValidity();
            signedCertificate.verify(issuerCertificate.getPublicKey());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static byte[] createDigitalSignature(byte[] data, PrivateKey privateKey) throws PKIException {
        try {
            Signature signature = Signature.getInstance(SHA256_RSA, BC_PROVIDER); //"SHA256withECDSA"
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (Exception e) {
            throw new PKIException(e);
        }
    }

    public static boolean verifyDigitalSignature(byte[] data, byte[] signatureData, X509Certificate certificate) throws PKIException {
        try {
            Signature signature = Signature.getInstance(SHA256_RSA, BC_PROVIDER); //"SHA256withECDSA"
            signature.initVerify(certificate);
            signature.update(data);
            return signature.verify(signatureData);
        } catch (Exception e) {
            throw new PKIException(e);
        }
    }

    public static byte[] encrypt(byte[] data, Key key) throws PKIException {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION, BC_PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new PKIException(e);
        }
    }

    public static byte[] decrypt(byte[] data, Key key) throws PKIException {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION, BC_PROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new PKIException(e);
        }
    }

    public static KeyPairHolder loadPrivateKeyAndCertificateFromJKS(String keystorePath, String alias, String keystorePassword, String privateKeyPassword) throws PKIException {
        try {
            KeyStore keystore = KeyStore.getInstance(KEYSTORE_TYPE);
            InputStream is = JCEUtils.class.getResourceAsStream(keystorePath);
            keystore.load(is, keystorePassword.toCharArray());
            X509Certificate certificate = (X509Certificate) keystore.getCertificate(alias);
            PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, privateKeyPassword.toCharArray());
            return new KeyPairHolder(privateKey, certificate);
        } catch (Exception e) {
            throw new PKIException(e);
        }
    }

    public static byte[] serializeX509Certificate(X509Certificate certificate) throws PKIException {
        try {
            return certificate.getEncoded();
        } catch(Exception e) {
            throw new PKIException(e);
        }
    }

    public static X509Certificate deserializeX509Certificate(byte[] data) throws PKIException {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(X509, BC_PROVIDER);
            return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(data));
        } catch(Exception e) {
            throw new PKIException(e);
        }
    }

    public static byte[] serializePrivateKey(PrivateKey privateKey) {
        return privateKey.getEncoded();
    }

    public static PrivateKey deserializePrivateKey(byte[] data) throws PKIException {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, BC_PROVIDER);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(data);
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch(Exception e) {
            throw new PKIException(e);
        }
    }

    public static byte[] createJKSWithPrivateKeyAndCertificate(String alias, String keystorePassword, String privateKeyPassword, KeyPairHolder keyPairHolder) throws PKIException {
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            Certificate[] certificates =  new Certificate[] { keyPairHolder.getCertificate() };
            KeyStore keystore = KeyStore.getInstance(KEYSTORE_TYPE);
            keystore.load(null, keystorePassword.toCharArray());
            keystore.setKeyEntry(alias, keyPairHolder.getPrivateKey(), privateKeyPassword.toCharArray(), certificates);
            keystore.store(bas, keystorePassword.toCharArray());
            bas.flush();
            return bas.toByteArray();
        } catch(Exception e) {
            throw new PKIException(e);
        }
    }

    public static KeyPairHolder loadKeypairFromJKS(String alias, String keystorePassword, String privateKeyPassword, byte[] jksData) throws PKIException {
        try {
            KeyStore keystore = KeyStore.getInstance(KEYSTORE_TYPE);
            ByteArrayInputStream bis =  new ByteArrayInputStream(jksData);
            keystore.load(bis, keystorePassword.toCharArray());
            X509Certificate certificate = (X509Certificate) keystore.getCertificate(alias);
            PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, privateKeyPassword.toCharArray());
            return new KeyPairHolder(privateKey, certificate);
        } catch(Exception e) {
            throw new PKIException(e);
        }
    }

}
