package itx.diffiehellman;

import java.math.BigInteger;
import java.util.Random;

public class SecretEntity {

    private final BigInteger g;
    private final BigInteger n;
    private final BigInteger p;

    /**
     * Initialize this Secret Entity with g and n.
     * @param g - a small random prime number
     * @param n - much larger than g prime number
     */
    public SecretEntity(BigInteger g, BigInteger n) {
        this.g = g;
        this.n = n;
        //initialize internal secret private key of this entity
        this.p = generateRandom();
    }

    /**
     * Generate (p^g mod n)
     * @return (p^g mod n)
     */
    public BigInteger getPG() {
        return g.modPow(p,  n);
    }

    /**
     * Generate secret for other (p^g mod n) value.
     * @param otherPG (p^g mod n) value from different SecretEntity using same g,n.
     * @return internal secret.
     */
    public BigInteger getSecret(BigInteger otherPG) {
        return otherPG.modPow(p,  n);
    }

    private BigInteger generateRandom() {
        Random random = new Random();
        BigInteger bigInteger = new BigInteger(16, random);
        return bigInteger.abs();
    }

}
