package itx.elastic.demo.tests;

import itx.elastic.demo.DataServiceImpl;
import itx.elastic.demo.Utils;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Optional;

import static itx.elastic.demo.tests.TestUtils.INDEX_NAME;

public class DataServiceIndexTestsIT {

    private DataServiceImpl dataService;

    @BeforeClass
    public void init() {
        dataService = new DataServiceImpl("localhost", 9200, "http");
    }

    @Test
    public void testDataService() {
        Optional<Boolean> isCreated = dataService.isIndexCreated(INDEX_NAME);
        Assert.assertNotNull(isCreated);
        Assert.assertNotNull(isCreated.isPresent());
    }

    @Test
    public void testDataServiceIndexCreateDelete() throws IOException {
        try {
            dataService.deleteIndex(INDEX_NAME);
        } catch (Exception e) {
        }

        Optional<CreateIndexResponse> createIndexResponse = dataService.createIndex(INDEX_NAME, Utils.createMapping());
        Assert.assertTrue(createIndexResponse.isPresent());
        Assert.assertTrue(createIndexResponse.get().isAcknowledged());

        Optional<Boolean> isCreated = dataService.isIndexCreated(INDEX_NAME);
        Assert.assertNotNull(isCreated.isPresent());
        Assert.assertTrue(isCreated.get());

        Optional<AcknowledgedResponse> deleteIndexResponse = dataService.deleteIndex(INDEX_NAME);
        Assert.assertTrue(deleteIndexResponse.isPresent());
        Assert.assertTrue(deleteIndexResponse.get().isAcknowledged());

        isCreated = dataService.isIndexCreated(INDEX_NAME);
        Assert.assertNotNull(isCreated.isPresent());
        Assert.assertFalse(isCreated.get());
    }

    @AfterClass
    public void shutdown() throws IOException {
        if (dataService != null) {
            dataService.close();
        }
    }

    public static void main(String[] args) throws IOException {
        DataServiceIndexTestsIT testsIT = new DataServiceIndexTestsIT();
        testsIT.init();
        //testsIT.testCreateDeleteDocuments();
        testsIT.shutdown();
    }

}
