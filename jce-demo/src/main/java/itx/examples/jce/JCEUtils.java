package itx.examples.jce;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
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

    public static KeyPair generateKeyPair() throws PKIException {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", BC_PROVIDER);
            SecureRandom secureRandom = SecureRandom.getInstance("NativePRNG");
            keyPairGenerator.initialize(2048, secureRandom);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new PKIException(e);
        }
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

}
