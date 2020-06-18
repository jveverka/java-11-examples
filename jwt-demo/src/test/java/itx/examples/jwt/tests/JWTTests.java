package itx.examples.jwt.tests;

import com.nimbusds.jose.JOSEException;
import itx.examples.jce.JCEUtils;
import itx.examples.jce.KeyPairHolder;
import itx.examples.jce.PKIException;
import itx.examples.jwt.JWTUtils;
import itx.examples.jwt.JWToken;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.security.Security;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JWTTests {

    private static Map<String, KeyPairHolder> keyCache;
    private static JWToken validJWToken;

    @BeforeAll
    public static void init() throws PKIException {
        Security.addProvider(new BouncyCastleProvider());
        keyCache = new HashMap<>();
        keyCache.put("key-001", JCEUtils.generateSelfSignedKeyPairHolder("issuer", new Date(), 1L, TimeUnit.HOURS));
        keyCache.put("key-002", JCEUtils.generateSelfSignedKeyPairHolder("issuer", new Date(), 1L, TimeUnit.HOURS));
    }

    @Test
    @Order(1)
    public void issueValidToken() throws JOSEException {
        KeyPairHolder keyPairHolder = keyCache.get("key-001");
        Long expires = Instant.now().getEpochSecond() + 3600L;
        validJWToken = JWTUtils.issue("user", "key-001", keyPairHolder.getPrivateKey(), expires);
        assertNotNull(validJWToken);
        assertNotNull(validJWToken.getToken());
    }

    @Test
    @Order(2)
    public void validateValidToken() throws ParseException, JOSEException {
        KeyPairHolder keyPairHolder = keyCache.get("key-001");
        boolean result = JWTUtils.validate(validJWToken, "user", "key-001", keyPairHolder.getCertificate());
        assertTrue(result);
    }

    @Test
    @Order(3)
    public void validateValidTokenUsingIncorrectCertificate() throws ParseException, JOSEException {
        KeyPairHolder keyPairHolder = keyCache.get("key-002");
        boolean result = JWTUtils.validate(validJWToken, "user", "key-002", keyPairHolder.getCertificate());
        assertFalse(result);
    }

    @Test
    @Order(4)
    public void issueAndTestExpiredToken() throws ParseException, JOSEException {
        KeyPairHolder keyPairHolder = keyCache.get("key-001");
        Long expires = Instant.now().getEpochSecond() - 3600L;
        validJWToken = JWTUtils.issue("user", "key-001", keyPairHolder.getPrivateKey(), expires);
        assertNotNull(validJWToken);
        assertNotNull(validJWToken.getToken());
        boolean result = JWTUtils.validate(validJWToken, "user", "key-001", keyPairHolder.getCertificate());
        assertFalse(result);
    }

}
