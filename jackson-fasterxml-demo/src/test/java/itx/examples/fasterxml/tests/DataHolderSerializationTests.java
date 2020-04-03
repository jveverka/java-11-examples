package itx.examples.fasterxml.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import itx.examples.fasterxml.services.DataService;
import itx.examples.fasterxml.services.DataServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataHolderSerializationTests {

    private static ObjectMapper mapper;
    private static String serialized;

    @BeforeAll
    public static void init() {
        mapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Test
    @Order(1)
    public void testSerialization() throws JsonProcessingException {
        DataService dataService = new DataServiceImpl();
        serialized = mapper.writeValueAsString(dataService);
        assertNotNull(serialized);
    }

    @Test
    @Order(2)
    public void testDeserialization() throws JsonProcessingException {
        DataService dataService = mapper.readValue(serialized, DataService.class);
        assertNotNull(dataService);
        assertNotNull(dataService.getData());
        assertTrue(dataService.getData().size() == 2);
    }

}