package itx.examples.sshd.tests;

import itx.ssh.server.utils.DataBuffer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataBufferTests {

    public static byte[] getInData(int size) {
        byte[] data = new byte[size];
        for (int i=0; i<size; i++) {
            data[i] = 'a';
        }
        return data;
    }

    @DataProvider(name = "testInputsProvider")
    public static Object[][] getDataForTestInputs() {
        return new Object[][] {
                { getInData(0  ),   0, 128 },
                { getInData(20 ),  20, 128 },
                { getInData(128), 128, 128 },
                { getInData(129), 129, 256 },
                { getInData(500), 500, 512 },
        };
    }

    @Test(dataProvider = "testInputsProvider")
    public void testDataBuffer(byte[] input, int expectedCursorWaterMark, int expectedMaxSize) {
        DataBuffer dataBuffer = new DataBuffer();
        for (int i=0; i<input.length; i++) {
            dataBuffer.add(input[i]);
        }
        byte[] data = dataBuffer.get();
        Assert.assertEquals(dataBuffer.getCursorWaterMark(), expectedCursorWaterMark);
        Assert.assertEquals(dataBuffer.getMaxSize(), expectedMaxSize);
        Assert.assertEquals(data.length, input.length);
        dataBuffer.reset();
        data = dataBuffer.get();
        Assert.assertEquals(dataBuffer.getCursorWaterMark(), 0);
        Assert.assertEquals(dataBuffer.getMaxSize(), expectedMaxSize);
        Assert.assertEquals(data.length, 0);
    }

}
