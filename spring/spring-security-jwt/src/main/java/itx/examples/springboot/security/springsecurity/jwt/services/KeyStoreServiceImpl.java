package itx.examples.springboot.security.springsecurity.jwt.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Service
public class KeyStoreServiceImpl implements KeyStoreService {

    private final KeyStore keystore;
    private final Key key;

    public KeyStoreServiceImpl() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        keystore = KeyStore.getInstance("JKS");
        InputStream is = KeyStoreServiceImpl.class.getResourceAsStream("/keystore.jks");
        keystore.load(is, "secret".toCharArray());
        key = (PrivateKey) keystore.getKey("organization", "secret".toCharArray());
    }

    @Override
    public Key getKey() {
        return key;
    }

}
