package itx.examples.kafka.client.tests;

import itx.examples.kafka.client.ClientApp;
import itx.examples.kafka.dto.ServiceRequest;
import itx.examples.kafka.dto.ServiceResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BasicTests {

    @Test
    public void testResponseRequestCompare() {
        ServiceRequest request = new ServiceRequest("taskid", "clientId","data");
        ServiceResponse response = new ServiceResponse("taskid", "clientId", "data", "response");
        String compare = ClientApp.checkResponse(request, response);
        Assert.assertEquals(compare, "OK");
    }

}
