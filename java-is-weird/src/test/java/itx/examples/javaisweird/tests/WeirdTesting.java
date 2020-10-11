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
        Integer a = 42;
        Integer b = 42;

        Integer c = 666;
        Integer d = 666;

        assertTrue(a == b);
        assertEquals(a, b);
        assertFalse(c == d);
        assertEquals(c, d);
    }

    @Test
    public void testCharArithmetic() {
        char ch = '0';
        ch *= 1.1;
        assertEquals('4', ch);
    }

}
