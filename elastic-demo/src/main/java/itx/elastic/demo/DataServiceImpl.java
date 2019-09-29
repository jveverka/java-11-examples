package itx.elastic.demo;

import itx.elastic.demo.dto.DataQuery;
import itx.elastic.demo.dto.EventData;
import itx.elastic.demo.dto.EventId;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class DataServiceImpl implements DataService, DataAdmin, Closeable {

    private final RestHighLevelClient client;

    public DataServiceImpl(String hostname, int port, String scheme) {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(hostname, port, scheme)
                )
        );
    }

    @Override
    public void saveData(EventData eventData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<EventData> getDataById(EventId id) {
        return Optional.empty();
    }

    @Override
    public void deleteData(EventData eventData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteData(Collection<EventId> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<EventData> getData(DataQuery dataQuery) {
        return Collections.emptyList();
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    @Override
    public Optional<CreateIndexResponse> createIndex(String indexName) {
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            return Optional.of(createIndexResponse);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Boolean> isIndexCreated(String indexName) {
        try {
            GetIndexRequest request = new GetIndexRequest(indexName);
            boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
            return Optional.of(Boolean.valueOf(exists));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AcknowledgedResponse> deleteIndex(String indexName) {
        try {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
            AcknowledgedResponse deleteResponse = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            return Optional.of(deleteResponse);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

}
