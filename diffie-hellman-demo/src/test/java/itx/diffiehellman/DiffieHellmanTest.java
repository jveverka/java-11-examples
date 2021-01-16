package itx.diffiehellman;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigInteger;

public class DiffieHellmanTest {

    //public randomly generated init values
    private final BigInteger g = new BigInteger("2287");         //a small random prime number
    private final BigInteger n = new BigInteger("1073676287");   //much larger than g prime number

    @Test(invocationCount = 100)
    public void diffieHellmanSecretKeyGenerationTest() {
        //1. initialize entities with same g and n numbers
        SecretEntity alice = new SecretEntity(g, n);
        SecretEntity bob   = new SecretEntity(g, n);

        //2. calculate (p^g mod n) of each entity
        BigInteger alicePG = alice.getPG();
        BigInteger bobPG   = bob.getPG();

        //3. exchange previously calculated (p^g mod n) between entities.
        //   both entities must calculate same secret independently.
        //   get secrets, both secrets must be equal.
        BigInteger aliceSecret = alice.getSecret(bobPG);
        BigInteger bobSecret = bob.getSecret(alicePG);

        //4. each entity must generate same internal secret.
        //   equality test for secrets.
        Assert.assertEquals(aliceSecret, bobSecret);

        //5. Test that other values are NOT equals.
        Assert.assertNotEquals(alicePG, bobPG);
        Assert.assertNotEquals(alicePG, aliceSecret);
        Assert.assertNotEquals(bobSecret, bobPG);
    }

}
