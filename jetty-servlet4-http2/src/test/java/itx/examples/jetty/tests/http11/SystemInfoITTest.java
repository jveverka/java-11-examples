package itx.examples.jetty.tests.http11;

import itx.examples.jetty.client.http11.SystemInfoServiceClient;
import itx.examples.jetty.common.SystemUtils;
import itx.examples.jetty.common.dto.SystemInfo;
import itx.examples.jetty.common.services.SystemInfoService;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.security.KeyStore;

public class SystemInfoITTest {

    @Test
    public void getSystemInfoTestHttp() {
        SystemInfoService systemInfoService = new SystemInfoServiceClient("http://localhost:8080");
        SystemInfo systemInfo = systemInfoService.getSystemInfo();
        Assert.assertNotNull(systemInfo);
        Assert.assertNotNull(systemInfo.getApplicationName());
        Assert.assertNotNull(systemInfo.getApplicationVersion());
        Assert.assertNotNull(systemInfo.getSystemTime());
        Assert.assertTrue(systemInfo.getSystemTime() > 0);
    }

    @Test
    public void getSystemInfoTestHttps() throws  Exception {
        KeyStore keyStore = SystemUtils.loadJKSKeyStore("client.jks", "secret");
        SystemInfoService systemInfoService = new SystemInfoServiceClient("https://localhost:8443", keyStore, "secret");
        SystemInfo systemInfo = systemInfoService.getSystemInfo();
        Assert.assertNotNull(systemInfo);
        Assert.assertNotNull(systemInfo.getApplicationName());
        Assert.assertNotNull(systemInfo.getApplicationVersion());
        Assert.assertNotNull(systemInfo.getSystemTime());
        Assert.assertTrue(systemInfo.getSystemTime() > 0);
    }

}
