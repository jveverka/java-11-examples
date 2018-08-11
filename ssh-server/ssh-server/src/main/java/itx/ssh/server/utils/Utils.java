package itx.ssh.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("do not instantiate utility class");
    }

    /**
     * Load {@link KeyPair} from JKS keystore.
     * @param is keystore data from {@link InputStream}.
     * @param keystorePassword password to open keystore.
     * @param keyPairAlias name of the keystore entry representing the {@link KeyPair}
     * @param keyPairPassword password for {@link KeyPair} entity
     * @return instance of {@link KeyPair}
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     */
    public static KeyPair loadKeyPair(InputStream is, String keystorePassword, String keyPairAlias, String keyPairPassword)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, keystorePassword.toCharArray());

        Key key = keystore.getKey(keyPairAlias, keyPairPassword.toCharArray());
        if (key instanceof PrivateKey) {
            Certificate cert = keystore.getCertificate(keyPairAlias);
            PublicKey publicKey = cert.getPublicKey();
            return new KeyPair(publicKey, (PrivateKey) key);
        }
        throw new UnrecoverableKeyException("KeyPair not found");
    }

    /**
     * Load {@link PublicKey} from JKS keystore.
     * @param is keystore data from {@link InputStream}.
     * @param keystorePassword password to open keystore.
     * @param publicKeyAlias name of the keystore entry representing the {@link PublicKey}
     * @return instance of {@link PublicKey}
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static PublicKey loadPublicKey(InputStream is, String keystorePassword, String publicKeyAlias)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, keystorePassword.toCharArray());

        Certificate cert = keystore.getCertificate(publicKeyAlias);
        return cert.getPublicKey();
    }

    /**
     * Generate one-time server keypair.
     * @return
     */
    public static Iterable<KeyPair> generateKeyPairs() {
        try {
            List<KeyPair> result = new ArrayList<>();
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair keyPair = keyGen.genKeyPair();
            result.add(keyPair);
            return Collections.unmodifiableCollection(result);
        } catch (NoSuchAlgorithmException e) {
            return Collections.emptyList();
        }
    }

}
