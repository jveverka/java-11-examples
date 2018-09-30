package itx.blockchain.test;

import itx.blockchain.basic.LedgerUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

public class UtilsTests {

    @DataProvider(name = "ArrayCompareData")
    public static Object[][] getArrayCompareData() {
        return new Object[][] {
                { new byte[] {}, null, Boolean.FALSE },
                { null, new byte[] {}, Boolean.FALSE },
                { new byte[] {}, new byte[] {}, Boolean.TRUE },
                { new byte[] { 0, 1, 2, 3 }, new byte[] { 0, 1, 2, 3 }, Boolean.TRUE },
                { new byte[] { 0, 1, 2, 3 }, new byte[] { 0, 1, 4, 5 }, Boolean.FALSE },
                { new byte[] { 0, 1, 2, 3 }, new byte[] { 4, 5 }, Boolean.FALSE },
                { new byte[] { 0, 1, 2, 3 }, new byte[] { 0, 1, 2, 3, 4, 5 }, Boolean.FALSE },
        };
    }

    @Test(dataProvider = "ArrayCompareData")
    public void testArrayCompare(byte[] array1, byte[] array2, Boolean expectedResult) {
        boolean equals = Arrays.equals(array1, array2);
        Assert.assertTrue(equals == expectedResult.booleanValue());
    }

    @DataProvider(name = "ArrayConcatData")
    public static Object[][] getArrayConcatData() {
        return new Object[][] {
                { new byte[] {}, new byte[] {}, new byte[] {} },
                { new byte[] { 0, 1, 2, 3 }, new byte[] { 0, 1, 2, 3 }, new byte[] { 0, 1, 2, 3, 0, 1, 2, 3 } },
                { new byte[] { 0, 1, 2, 3 }, new byte[] { 4, 5, 6, 7 }, new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 } },
                { new byte[] { 0, 1, 2, 3 }, new byte[] { }, new byte[] { 0, 1, 2, 3 } },
                { new byte[] { }, new byte[] { 4, 5, 6, 7 }, new byte[] { 4, 5, 6, 7 } },
                { new byte[] { 0, 1 }, new byte[] { 4, 5, 6, 7 }, new byte[] { 0, 1, 4, 5, 6, 7 } },
                { new byte[] { 0, 1, 2, 3 }, new byte[] { 4, 5 }, new byte[] { 0, 1, 2, 3, 4, 5 } },
        };
    }

    @Test(dataProvider = "ArrayConcatData")
    public void testArrayConcat(byte[] array1, byte[] array2, byte[] concatArray) {
        byte[] combine = LedgerUtils.combine(array1, array2);
        boolean equals = Arrays.equals(combine, concatArray);
        Assert.assertTrue(equals);
    }

    @Test
    public void testHexBytes() {
        int dataLength = 1024;
        byte[] dataIn = new byte[dataLength];
        for (int i=0; i< dataLength; i++) {
            dataIn[i] = (byte)i;
        }
        String encodedData = LedgerUtils.bytesToHex(dataIn);
        byte[] dataOut = LedgerUtils.hexToBytes(encodedData);
        boolean equals = Arrays.equals(dataIn, dataOut);
        Assert.assertTrue(equals);
    }

}
