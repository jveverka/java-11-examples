package itx.examples.cachedmodel.persistence;

import itx.examples.cachedmodel.model.keys.CK;

public interface PersistenceService {

    <T> void onNodeCreated(T newNode, CK<T> ck);

    <T> void onNodeUpdated(T oldNode, T newNode, CK<T> ck);

    <T> void onNodeDeleted(T oldNode, CK<T> ck);

}
