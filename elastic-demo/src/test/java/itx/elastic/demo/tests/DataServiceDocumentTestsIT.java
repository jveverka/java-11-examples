package itx.elastic.demo.tests;

import itx.elastic.demo.DataServiceImpl;
import itx.elastic.demo.Utils;
import itx.elastic.demo.dto.EventData;
import itx.elastic.demo.dto.EventDataInfo;
import itx.elastic.demo.dto.EventId;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import static itx.elastic.demo.tests.TestUtils.INDEX_NAME;

public class DataServiceDocumentTestsIT {

    private DataServiceImpl dataService;

    @BeforeClass
    public void init() {
        dataService = new DataServiceImpl("localhost", 9200, "http");
    }

    @BeforeMethod
    public void testInit() throws IOException {
        try {
            dataService.deleteIndex(INDEX_NAME);
        } catch (Exception e) {
        }
        Optional<CreateIndexResponse> createIndexResponse = dataService.createIndex(INDEX_NAME, Utils.createMapping());
        Assert.assertTrue(createIndexResponse.isPresent());
        Assert.assertTrue(createIndexResponse.get().isAcknowledged());
    }

    @Test
    public void testCreateDeleteDocumentClientId() throws IOException {
        EventData eventData01Original = TestUtils.createEventData(10);

        boolean result = dataService.saveData(eventData01Original);
        Assert.assertTrue(result);

        Optional<EventData> eventData01result = dataService.getDataById(eventData01Original.getId());
        Assert.assertTrue(eventData01result.isPresent());
        Assert.assertEquals(eventData01result.get().getId(), eventData01Original.getId());

        result = dataService.deleteData(eventData01Original.getId());
        Assert.assertTrue(result);
    }

    @Test
    public void testCreateDeleteDocumentServerId() throws IOException {
        EventDataInfo eventDataInfo = TestUtils.createEventDataInfo(11);
        Optional<EventId> eventIdResult = dataService.saveData(eventDataInfo);
        Assert.assertTrue(eventIdResult.isPresent());

        Optional<EventData> eventData02result = dataService.getDataById(eventIdResult.get());
        Assert.assertTrue(eventData02result.isPresent());
        Assert.assertEquals(eventData02result.get().getId(), eventIdResult.get());

        boolean result = dataService.deleteData(eventIdResult.get());
        Assert.assertTrue(result);
    }

    @Test
    public void testDeleteNotExistingDocumentById() throws IOException {
        EventId id = new EventId("not-existing-id");
        boolean result = dataService.deleteData(id);
        Assert.assertFalse(result);
    }

    @Test
    public void testGetNotExistingDocumentById() throws IOException {
        EventId id = new EventId("not-existing-id");
        Optional<EventData> eventData02result = dataService.getDataById(id);
        Assert.assertFalse(eventData02result.isPresent());
    }

    @Test
    public void testGetAllDocuments() throws IOException {
        boolean result = false;
        EventData eventData10Original = TestUtils.createEventData(10);
        EventData eventData11Original = TestUtils.createEventData(11);
        result = dataService.saveData(eventData10Original);
        Assert.assertTrue(result);
        result  = dataService.saveData(eventData11Original);
        Assert.assertTrue(result);

        Collection<EventData> dataAll = dataService.getDataAll();
        Assert.assertNotNull(dataAll);
        Assert.assertEquals(dataAll.size(), 2, "expected at least 2 elements");
    }

    @AfterMethod
    public void testShutdown() {
        Optional<AcknowledgedResponse> deleteIndexResponse = dataService.deleteIndex(INDEX_NAME);
        Assert.assertTrue(deleteIndexResponse.isPresent());
        Assert.assertTrue(deleteIndexResponse.get().isAcknowledged());
    }

    @AfterClass
    public void shutdown() throws IOException {
        if (dataService != null) {
            dataService.close();
        }
    }

    public static void main(String[] args) throws IOException {
        DataServiceDocumentTestsIT testsIT = new DataServiceDocumentTestsIT();
        testsIT.init();
        testsIT.testInit();
        testsIT.testGetAllDocuments();
        testsIT.testShutdown();
        testsIT.shutdown();
    }
}
