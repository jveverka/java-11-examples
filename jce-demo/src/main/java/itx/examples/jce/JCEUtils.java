package itx.examples.jce;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

public final class JCEUtils {

    private JCEUtils() {
        throw new UnsupportedOperationException();
    }

    private static final String BC_PROVIDER = "BC";

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", BC_PROVIDER);
        SecureRandom secureRandom = SecureRandom.getInstance("NativePRNG");
        keyPairGenerator.initialize(2048, secureRandom);
        return keyPairGenerator.generateKeyPair();
    }

    public static X509Certificate createSignedCertificate(String issuerName, String subjectName, Long notBeforeTimestamp, Long validDuration, PublicKey publicKey, PrivateKey privateKey) throws OperatorCreationException, IOException, CertificateException, NoSuchProviderException {
        X500Name issuer = new X500Name("CN=" + issuerName);
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date notBefore = new Date(notBeforeTimestamp);
        Date notAfter = new Date(notBeforeTimestamp + validDuration);
        X500Name subject = new X500Name("CN=" + subjectName);
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(issuer, serial, notBefore, notAfter, subject, publicKeyInfo);
        JcaContentSignerBuilder jcaContentSignerBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        ContentSigner signer = jcaContentSignerBuilder.build(privateKey);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", BC_PROVIDER);
        byte[] certBytes = certBuilder.build(signer).getEncoded();
        return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certBytes));
    }

    public static X509Certificate createSelfSignedCertificate(String issuerAndSubject, Long notBeforeTimestamp, Long validDuration, KeyPair keyPair) throws OperatorCreationException, IOException, CertificateException, NoSuchProviderException {
        return createSignedCertificate(issuerAndSubject, issuerAndSubject, notBeforeTimestamp, validDuration, keyPair.getPublic(), keyPair.getPrivate());
    }

    public static void verifyCertificate(X509Certificate clientCertificate, X509Certificate caCertificate) throws CertificateException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        caCertificate.checkValidity();
        clientCertificate.checkValidity();
        clientCertificate.verify(caCertificate.getPublicKey());
    }

    public static byte[] createDigitalSignature(byte[] data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException {
        Signature signature = Signature.getInstance("SHA256withRSA", BC_PROVIDER); //"SHA256withECDSA"
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public static boolean verifyDigitalSignature(byte[] data, byte[] signatureData, X509Certificate certificate) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException {
        Signature signature = Signature.getInstance("SHA256withRSA", BC_PROVIDER); //"SHA256withECDSA"
        signature.initVerify(certificate);
        signature.update(data);
        return signature.verify(signatureData);
    }

    public static byte[] encrypt(byte[] data, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {
        Cipher cipher = Cipher.getInstance("RSA/None/OAEPWITHSHA-256ANDMGF1PADDING", BC_PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {
        Cipher cipher = Cipher.getInstance("RSA/None/OAEPWITHSHA-256ANDMGF1PADDING", BC_PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

}
