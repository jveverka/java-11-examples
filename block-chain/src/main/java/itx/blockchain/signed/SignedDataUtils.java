package itx.blockchain.signed;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public final class SignedDataUtils {

    private SignedDataUtils() {
        throw new UnsupportedOperationException("Please, do not instantiate utility class !");
    }

    public static SignedData createSignedData(byte[] data, PrivateKey privateKey, String algorithm)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(data);
        byte[] sig = signature.sign();
        return new SignedDataImpl(algorithm, data, sig);
    }

    public static boolean verifySignedData(SignedData signedData, Certificate certificate)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(signedData.getAlgorithm());
        signature.initVerify(certificate);
        signature.update(signedData.getData());
        return signature.verify(signedData.getSignature());
    }

    public static PrivateKey getPrivateKey(InputStream keyStoreData, String keyStorePassword,
                                     String privateKeyAlias, String privateKeyPassword)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(keyStoreData, keyStorePassword.toCharArray());
        Key key = keystore.getKey(privateKeyAlias, privateKeyPassword.toCharArray());
        if (key instanceof PrivateKey) {
            return (PrivateKey)key;
        } else {
            throw new UnrecoverableKeyException();
        }
    }

    public static Certificate getCertificate(InputStream keyStoreData, String keyStorePassword, String certificateAlias)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(keyStoreData, keyStorePassword.toCharArray());
        Certificate certificate = keystore.getCertificate(certificateAlias);
        return certificate;
    }

}
