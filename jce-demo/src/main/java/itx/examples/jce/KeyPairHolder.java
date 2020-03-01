package itx.examples.jce;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

public class KeyPairHolder {

    private final KeyPair cAKeyPair;
    private final X509Certificate cACertificate;

    public KeyPairHolder(KeyPair caKeyPair, X509Certificate caCertificate) {
        cAKeyPair = caKeyPair;
        cACertificate = caCertificate;
    }

    public KeyPair getCAKeyPair() {
        return cAKeyPair;
    }

    public X509Certificate getCACertificate() {
        return cACertificate;
    }

}
