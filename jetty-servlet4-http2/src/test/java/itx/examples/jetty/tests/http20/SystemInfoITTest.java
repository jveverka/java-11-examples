package itx.examples.jetty.tests.http20;

import itx.examples.jetty.client.http20.SystemInfoServiceClient;
import itx.examples.jetty.common.SystemUtils;
import itx.examples.jetty.common.dto.SystemInfo;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.security.KeyStore;

public class SystemInfoITTest {

    @Test
    public void getSystemInfoTest() throws Exception {
        KeyStore keyStore = SystemUtils.loadJKSKeyStore("client.jks", "secret");
        itx.examples.jetty.common.services.SystemInfoService systemInfoService = new SystemInfoServiceClient("localhost", 8443, keyStore, "secret");
        SystemInfo systemInfo = systemInfoService.getSystemInfo();
        Assert.assertNotNull(systemInfo);
        Assert.assertNotNull(systemInfo.getApplicationName());
        Assert.assertNotNull(systemInfo.getApplicationVersion());
        Assert.assertNotNull(systemInfo.getSystemTime());
        Assert.assertTrue(systemInfo.getSystemTime() > 0);
    }

}
