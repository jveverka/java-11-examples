package itx.ssh.client;

import itx.ssh.server.utils.Utils;
import org.apache.sshd.client.keyverifier.RequiredServerKeyVerifier;
import org.apache.sshd.client.keyverifier.ServerKeyVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;

public class ServerKeyVerifierBuilder {

    private InputStream is;
    private String keystorePassword;
    private String publicKeyAlias;

    public ServerKeyVerifierBuilder() {
    }

    public ServerKeyVerifierBuilder setIs(InputStream is) {
        this.is = is;
        return this;
    }

    public ServerKeyVerifierBuilder setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
        return this;
    }

    public ServerKeyVerifierBuilder setPublicKeyAlias(String publicKeyAlias) {
        this.publicKeyAlias = publicKeyAlias;
        return this;
    }

    public ServerKeyVerifier build() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        PublicKey publicKey = Utils.loadPublicKey(is, keystorePassword, publicKeyAlias);
        RequiredServerKeyVerifier requiredServerKeyVerifier = new RequiredServerKeyVerifier(publicKey);
        return requiredServerKeyVerifier;
    }

}
