package itx.examples.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import net.minidev.json.JSONObject;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;

public class JWTUtils {

    private JWTUtils()  {
    }

    public static JWToken issue(String subject, String keyId, PrivateKey privateKey, Long expires) throws JOSEException {

        JSONObject payload = new JSONObject();
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256, JOSEObjectType.JWT, null, null, null, null, null, null, null, null, keyId, null, null);
        payload.put("sub", subject);
        payload.put("exp", expires);
        JWSObject jwsObject = new JWSObject(header, new Payload(payload));
        jwsObject.sign(new RSASSASigner(privateKey));
        return new JWToken(jwsObject.serialize());
    }

    public static boolean validate(JWToken jwToken, String subject, String keyId, X509Certificate certificate) throws ParseException, JOSEException {
        RSASSAVerifier verifier = new RSASSAVerifier((RSAPublicKey)certificate.getPublicKey());
        SignedJWT signedJWT = SignedJWT.parse(jwToken.getToken());
        boolean verified = signedJWT.verify(verifier);
        String sub = signedJWT.getJWTClaimsSet().getSubject();
        String kid = signedJWT.getHeader().getKeyID();
        Date expires = signedJWT.getJWTClaimsSet().getExpirationTime();
        Date nowDate = new Date();
        boolean expired = nowDate.getTime() > expires.getTime();
        return verified && subject.equals(sub) && keyId.equals(kid) && !expired;
    }

}
