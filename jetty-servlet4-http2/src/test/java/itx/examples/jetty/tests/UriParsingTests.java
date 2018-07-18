package itx.examples.jetty.tests;

import itx.examples.jetty.common.SystemUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UriParsingTests {

    @DataProvider(name = "URIParsingTest")
    public static Object[][] credentials() {
        return new Object[][] {
                { "/data/sync", "/data/sync", new String[] { } },
                { "/data/sync", "/data/sync/", new String[] { } },
                { "/data/sync", "/data/sync/param1", new String[] { "param1" } },
                { "/data/sync", "/data/sync/param1/param2", new String[] { "param1", "param2" } },
                { "/data/sync", "/data/sync/param1/param2/param3", new String[] { "param1", "param2", "param3" } }
        };
    }

    @Test(dataProvider = "URIParsingTest")
    public void testUriParsing(String baseURI, String uri, String[] expectedParameters) {
        String[] urlParameters = SystemUtils.getURLParameters(baseURI, uri);
        Assert.assertNotNull(urlParameters);
        Assert.assertTrue(urlParameters.length == expectedParameters.length);
        for (int i=0; i<expectedParameters.length; i++) {
            Assert.assertEquals(urlParameters[i], expectedParameters[i]);
        }
    }

}
