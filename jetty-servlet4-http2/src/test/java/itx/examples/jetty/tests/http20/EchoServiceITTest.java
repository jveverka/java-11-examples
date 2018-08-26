package itx.examples.jetty.tests.http20;

import itx.examples.jetty.client.http20.EchoServiceClient;
import itx.examples.jetty.common.SystemUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.security.KeyStore;
import java.util.Arrays;
import java.util.List;

public class EchoServiceITTest {

    @Test
    public void echoTest() throws Exception {
        KeyStore keyStore = SystemUtils.loadJKSKeyStore("client.jks", "secret");
        EchoServiceClient echoService = new EchoServiceClient("localhost", 8443, keyStore, "secret");
        echoService.connect();
        String echoResponse = echoService.ping("hello");
        Assert.assertNotNull(echoResponse);
        Assert.assertEquals(echoResponse, "echo:hello");
        echoService.close();
    }

    @Test
    public void echoRepeatTest() throws Exception {
        KeyStore keyStore = SystemUtils.loadJKSKeyStore("client.jks", "secret");
        EchoServiceClient echoService = new EchoServiceClient("localhost", 8443, keyStore, "secret");
        echoService.connect();
        List<String> requests = Arrays.asList(new String[] { "hello0", "hello1", "hello2", "hello3", "hello4", "hello5" });
        List<String> responses = echoService.ping(requests);
        Assert.assertNotNull(responses);
        Assert.assertTrue(responses.size() == requests.size());
        for (int i=0; i<requests.size(); i++) {
            Assert.assertEquals("echo:" + requests.get(i), responses.get(i));
        }
        echoService.close();
    }

}
