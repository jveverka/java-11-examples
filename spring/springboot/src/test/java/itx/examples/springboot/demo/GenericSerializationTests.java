package itx.examples.springboot.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import itx.examples.springboot.demo.dto.generic.GenericRequest;
import itx.examples.springboot.demo.dto.generic.SimpleDataPayload;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class GenericSerializationTests {

    @Test
    public void testReadWrite() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        GenericRequest<SimpleDataPayload> request = new GenericRequest("xxx", new SimpleDataPayload("zzz"));
        String jsonData = objectMapper.writeValueAsString(request);

        Assert.assertNotNull(jsonData);

        GenericRequest genericRequest = objectMapper.readValue(jsonData, GenericRequest.class);
        Assert.assertNotNull(genericRequest);
        Assert.assertTrue(genericRequest.getData() instanceof SimpleDataPayload);
    }

}
