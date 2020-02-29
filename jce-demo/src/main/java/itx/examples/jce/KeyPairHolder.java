package itx.examples.jce;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

public class KeyPairHolder {

    private final KeyPair CAKeyPair;
    private final X509Certificate CACertificate;

    public KeyPairHolder(KeyPair caKeyPair, X509Certificate caCertificate) {
        CAKeyPair = caKeyPair;
        CACertificate = caCertificate;
    }

    public KeyPair getCAKeyPair() {
        return CAKeyPair;
    }

    public X509Certificate getCACertificate() {
        return CACertificate;
    }

}
