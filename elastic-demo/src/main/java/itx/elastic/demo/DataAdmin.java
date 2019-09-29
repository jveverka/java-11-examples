package itx.elastic.demo;

import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.util.Optional;

public interface DataAdmin {

    /**
     * Check if given index exists.
     * @param indexName
     * @return
     */
    Optional<Boolean> isIndexCreated(String indexName);

    /**
     * Create index with mapping.
     * @param indexName
     * @param mapping
     * @return
     */
    Optional<CreateIndexResponse> createIndex(String indexName, XContentBuilder mapping);

    /**
     * Remove index.
     * @param indexName
     * @return
     */
    Optional<AcknowledgedResponse> deleteIndex(String indexName);

}
