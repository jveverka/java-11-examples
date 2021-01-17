package itx.entropy.tests;


import itx.entropy.EntropyCalculator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class EntropyTests {

    @DataProvider(name = "EntropyStringData")
    public static Object[][] getEntropyStringData() {
        return new Object[][]{
                { null, 0D },
                { "", 0D },
                { "A", 0D },
                { "AA", 0D },
                { "AAA", 0D },
                { "AB", 1D },
                { "ABB", 0.9182958340544894D },
                { "long data text", 3.1820058147602124D },
                { "aeSauyaepee7ohsooTee5noquequeezu", 3.3973683589017414D },
                { "ephai8agh9eiYa5r", 3.327819531114783D },
                { "abcdefghijklmnopqrstuvwyz", 4.643856189774723D }
        };
    }

    @Test(dataProvider = "EntropyStringData")
    public void testStringEntropyCalculation(String data, Double expectedEntropy) {
        Double entropy = EntropyCalculator.calculateEntropy(data);
        Assert.assertEquals(entropy, expectedEntropy);
    }

    @DataProvider(name = "EntropyIntegerData")
    public static Object[][] getEntropyIntegerData() {
        return new Object[][]{
                { null, 0D },
                { 0, 0D },
                { 1, 0D },
                { 11, 0D },
                { 12, 1D },
                { 122, 0.9182958340544894D },
                { 2020, 1D },
                { 12345, 2.321928094887362D },
                { 1234567890, 3.321928094887362D }
        };
    }

    @Test(dataProvider = "EntropyIntegerData")
    public void testIntegerEntropyCalculation(Integer data, Double expectedEntropy) {
        Double entropy = EntropyCalculator.calculateEntropy(data);
        Assert.assertEquals(entropy, expectedEntropy);
    }

    @DataProvider(name = "EntropyLongData")
    public static Object[][] getEntropyLongData() {
        return new Object[][]{
                { null, 0D },
                { 0L, 0D },
                { 1L, 0D },
                { 11L, 0D },
                { 12L, 1D },
                { 122L, 0.9182958340544894D },
                { 2020L, 1D },
                { 12345L, 2.321928094887362D },
                { 1234567890L, 3.321928094887362D },
                { 12345678901L, 3.2776134368191157D },
                { 123456789012L, 3.2516291673878226D }
        };
    }

    @Test(dataProvider = "EntropyLongData")
    public void testLongEntropyCalculation(Long data, Double expectedEntropy) {
        Double entropy = EntropyCalculator.calculateEntropy(data);
        Assert.assertEquals(entropy, expectedEntropy);
    }

}
