package itx.examples.javaisweird.tests;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class WeirdTesting {

    @Test
    public void testBigDecimalCompare() {
        BigDecimal x = new BigDecimal("1");
        BigDecimal y = new BigDecimal("1.00");
        assertFalse(x.equals(y));
        assertTrue(x.compareTo(y) == 0);
    }

    @Test
    public void testIntegerCompare() {
        Integer a = 100;
        Integer b = 100;

        Integer c = 200;
        Integer d = 200;

        assertTrue(a == b);
        assertEquals(a, b);
        assertFalse(c == d);
        assertEquals(c, d);
    }

}
