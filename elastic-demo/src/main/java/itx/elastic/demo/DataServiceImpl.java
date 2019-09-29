package itx.elastic.demo;

import itx.elastic.demo.dto.EventData;
import itx.elastic.demo.dto.EventDataInfo;
import itx.elastic.demo.dto.EventId;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class DataServiceImpl implements DataService, DataAdmin, Closeable {

    private final RestHighLevelClient client;
    private final static String INDEX_NAME = "events";

    public DataServiceImpl(String hostname, int port, String scheme) {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(hostname, port, scheme)
                )
        );
    }

    @Override
    public boolean saveData(EventData eventData) throws IOException {
        IndexRequest indexRequest = new IndexRequest(INDEX_NAME);
        indexRequest.id(eventData.getId().getId());
        indexRequest.source(Utils.createContent(eventData));
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        return RestStatus.CREATED.equals(indexResponse.status());
    }

    @Override
    public Optional<EventId> saveData(EventDataInfo eventDataInfo) throws IOException {
        IndexRequest indexRequest = new IndexRequest(INDEX_NAME);
        indexRequest.source(Utils.createContent(eventDataInfo));
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        if (RestStatus.CREATED.equals(indexResponse.status())) {
            return Optional.of(new EventId(indexResponse.getId()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<EventData> getDataById(EventId id) throws IOException {
        GetRequest getRequest = new GetRequest(INDEX_NAME, id.getId());
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        if (getResponse.isExists()) {
            return Optional.of(Utils.createFromSource(getResponse.getId(), getResponse.getSource()));
        }
        return Optional.empty();
    }

    @Override
    public Collection<EventData> getDataAll() throws IOException {
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        List<EventData> results = new ArrayList<>();
        Iterator<SearchHit> iterator = searchResponse.getHits().iterator();
        while (iterator.hasNext()) {
            SearchHit hit = iterator.next();
            EventData eventData = Utils.createFromSource(hit.getId(), hit.getSourceAsMap());
            results.add(eventData);
        }
        return results;
    }

    @Override
    public boolean deleteData(EventId id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX_NAME, id.getId());
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        return RestStatus.OK.equals(deleteResponse.status());
    }

    @Override
    public Collection<EventData> getData(SearchSourceBuilder searchSourceBuilder) throws IOException {
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        searchResponse.getHits();
        List<EventData> results = new ArrayList<>();
        Iterator<SearchHit> iterator = searchResponse.getHits().iterator();
        while (iterator.hasNext()) {
            SearchHit hit = iterator.next();
            EventData eventData = Utils.createFromSource(hit.getId(), hit.getSourceAsMap());
            results.add(eventData);
        }
        return results;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    @Override
    public Optional<CreateIndexResponse> createIndex(String indexName, XContentBuilder mapping) {
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.mapping(mapping);
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
