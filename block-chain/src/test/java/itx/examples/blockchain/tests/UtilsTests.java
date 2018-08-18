package itx.examples.blockchain.tests;

import itx.examples.blockchain.CommonUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UtilsTests {

    @DataProvider(name = "sha256data")
    public static Object[][] getSha256data() {
        return new Object[][] {
                { "", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855" },
                { "a", "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb" },
                { "data", "3a6eb0790f39ac87c94f3856b2dd2c5d110e6811602261a9a923d3bb23adc8b7" }
        };
    }

    @Test(dataProvider = "sha256data")
    public void testSha256(String data, String expectedHash) {
        String sha256Hash = CommonUtils.createSHA256Hash(data);
        Assert.assertEquals(sha256Hash, expectedHash);
    }

}
