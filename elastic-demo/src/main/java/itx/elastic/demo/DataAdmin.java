package itx.elastic.demo;

import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.util.Optional;

public interface DataAdmin {

    Optional<Boolean> isIndexCreated(String indexName);

    Optional<CreateIndexResponse> createIndex(String indexName);

    Optional<AcknowledgedResponse> deleteIndex(String indexName);

}
