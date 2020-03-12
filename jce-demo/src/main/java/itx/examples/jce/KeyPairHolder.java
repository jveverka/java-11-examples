package itx.examples.jce;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class KeyPairHolder {

    private final PrivateKey privateKey;
    private final X509Certificate certificate;

    public KeyPairHolder(PrivateKey privateKey, X509Certificate certificate) {
        this.privateKey = privateKey;
        this.certificate = certificate;
    }

    public KeyPair getKeyPair() {
        return new KeyPair(certificate.getPublicKey(), privateKey);
    }

    public X509Certificate getCertificate() {
        return certificate;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return certificate.getPublicKey();
    }

}
