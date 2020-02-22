package itx.examples.springboot.security.springsecurity.jwt.tests;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Assert;
import itx.examples.springboot.security.springsecurity.jwt.services.KeyStoreService;
import itx.examples.springboot.security.springsecurity.jwt.services.KeyStoreServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

public class JWTTokenTests {

    @Test
    public void testCreateAndVerifyJWTToken() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        KeyStoreService keyStoreService = new KeyStoreServiceImpl();
        long nowDate = LocalDate.now().toEpochSecond(LocalTime.now(), ZoneOffset.ofHours(0))*1000;
        long expirationDate = (nowDate + 3600*24)*1000;
        String userName = "UserName";
        String issuer = "Issuer";
        List<String> roles = Lists.list("ROLE_USER", "ROLE_ADMIN");
        Key key = keyStoreService.createUserKey(userName);
        Assert.notNull(key);

        //1. create JWT token
        String jwtToken = Jwts.builder()
                .setSubject(userName)
                .signWith(key)
                .setExpiration(new Date(expirationDate))
                .setIssuer(issuer)
                .setIssuedAt(new Date(nowDate))
                .claim("roles", roles)
                .compact();
        Assert.notNull(jwtToken);

        //2. verify JWT token
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken);
        Assert.notNull(claimsJws);
        List<String> rolesFromJWT = (List<String>)claimsJws.getBody().get("roles");
        Assert.notNull(rolesFromJWT);
        Assert.isTrue(rolesFromJWT.size() == 2);

        String subjectFromJWT = claimsJws.getBody().getSubject();
        Assert.isTrue(userName.equals(subjectFromJWT));

        String issuerFromJWT = claimsJws.getBody().getIssuer();
        Assert.isTrue(issuer.equals(issuerFromJWT));
    }

    @Test
    public void createUserCertificate() throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        KeyStoreService keyStoreService = new KeyStoreServiceImpl();
        Key userName = keyStoreService.createUserKey("UserName");
        Assert.notNull(userName);
    }


}
