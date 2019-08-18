package itx.ssh.server.auth;

import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;

public class KeyPairProviderImpl implements KeyPairProvider {

    final private static Logger LOG = LoggerFactory.getLogger(KeyPairProviderImpl.class);

    private Iterable<KeyPair> keyPairs;

    public KeyPairProviderImpl(Iterable<KeyPair> keyPairs) {
        LOG.info("loading keys ...");
        this.keyPairs = keyPairs;
    }

    @Override
    public Iterable<KeyPair> loadKeys() {
        return this.keyPairs;
    }

}
