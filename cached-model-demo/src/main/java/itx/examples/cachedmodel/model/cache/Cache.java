package itx.examples.cachedmodel.model.cache;

import itx.examples.cachedmodel.model.keys.CK;

import java.util.Collection;
import java.util.Optional;

public interface Cache {

    <T> void put(CK<T> ck, T data);

    <T> Optional<T> get(CK<T> ck);

    <T> Collection<T> getAll(Class<T> type);

    <T> void remove(CK<T> ck);

    void apply(Collection<OperationsBuilder.Operation> operations);

}
