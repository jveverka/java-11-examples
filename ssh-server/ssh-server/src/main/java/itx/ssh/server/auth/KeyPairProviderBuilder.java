package itx.ssh.server.auth;

import itx.ssh.server.utils.Utils;
import org.apache.sshd.common.keyprovider.KeyPairProvider;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

public class KeyPairProviderBuilder {

    private InputStream is;
    private String keystorePassword;
    private String keyPairAlias;
    private String keyPairPassword;

    public KeyPairProviderBuilder() {
    }

    public KeyPairProviderBuilder setIs(InputStream is) {
        this.is = is;
        return this;
    }

    public KeyPairProviderBuilder setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
        return this;
    }

    public KeyPairProviderBuilder setKeyPairAlias(String keyPairAlias) {
        this.keyPairAlias = keyPairAlias;
        return this;
    }

    public KeyPairProviderBuilder setKeyPairPassword(String keyPairPassword) {
        this.keyPairPassword = keyPairPassword;
        return this;
    }

    public KeyPairProvider build()
            throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        KeyPair keyPair = Utils.loadKeyPair(is, keystorePassword, keyPairAlias, keyPairPassword);
        List<KeyPair> keyPairList = new ArrayList<>();
        keyPairList.add(keyPair);
        return new KeyPairProviderImpl(keyPairList);
    }

}
