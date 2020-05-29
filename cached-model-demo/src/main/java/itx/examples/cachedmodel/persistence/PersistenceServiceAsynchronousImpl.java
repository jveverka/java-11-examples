package itx.examples.cachedmodel.persistence;

import itx.examples.cachedmodel.model.keys.CK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistenceServiceAsynchronousImpl implements PersistenceService {

    private static final Logger LOG = LoggerFactory.getLogger(PersistenceServiceAsynchronousImpl.class);

    @Override
    public <T> void onNodeCreated(T newNode, CK<T> ck) {
        LOG.info("onNodeCreated ck={} newNode={}", ck, newNode);
    }

    @Override
    public <T> void onNodeUpdated(T oldNode, T newNode, CK<T> ck) {
        LOG.info("onNodeUpdated ck={} oldNode={} newNode={}", ck, oldNode, newNode);
    }

    @Override
    public <T> void onNodeDeleted(T oldNode, CK<T> ck) {
        LOG.info("onNodeDeleted ck={} oldNode={}", ck, oldNode);
    }

}
