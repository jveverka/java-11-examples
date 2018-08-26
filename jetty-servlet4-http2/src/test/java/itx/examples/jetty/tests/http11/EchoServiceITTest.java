package itx.examples.jetty.tests.http11;

import itx.examples.jetty.client.http11.EchoServiceClient;
import itx.examples.jetty.common.SystemUtils;
import itx.examples.jetty.common.services.EchoService;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.security.KeyStore;

public class EchoServiceITTest {

    @Test
    public void echoTestHttp() throws Exception {
        EchoService echoService = new EchoServiceClient("http://localhost:8080");
        String response = echoService.ping("hello");
        Assert.assertNotNull(response);
        Assert.assertEquals(response, "echo:hello");
    }

    @Test
    public void echoTestHttps() throws Exception {
        KeyStore keyStore = SystemUtils.loadJKSKeyStore("client.jks", "secret");
        EchoService echoService = new EchoServiceClient("https://localhost:8443", keyStore, "secret");
        String response = echoService.ping("hello");
        Assert.assertNotNull(response);
        Assert.assertEquals(response, "echo:hello");
    }

}
