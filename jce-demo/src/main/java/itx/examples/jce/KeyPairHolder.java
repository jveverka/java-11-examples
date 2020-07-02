package itx.examples.jce;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyPairHolder that = (KeyPairHolder) o;
        return Objects.equals(privateKey, that.privateKey) &&
                Objects.equals(certificate, that.certificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(privateKey, certificate);
    }

}
