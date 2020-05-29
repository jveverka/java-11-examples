package itx.examples.cachedmodel.model.cache;

import itx.examples.cachedmodel.model.keys.CK;
import itx.examples.cachedmodel.persistence.PersistenceService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CacheImpl implements Cache {

    private  final PersistenceService persistenceService;
    private final Map<CK, Object> objects;

    public CacheImpl(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
        this.objects = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized <T> void put(CK<T> ck, T newData) {
        T oldData = (T)objects.put(ck, newData);
        if (oldData == null) {
            persistenceService.onNodeCreated(newData, ck);
        } else {
            persistenceService.onNodeUpdated(oldData, newData, ck);
        }
    }

    @Override
    public synchronized <T> Optional<T> get(CK<T> ck) {
        return Optional.ofNullable((T)objects.get(ck));
    }

    @Override
    public synchronized <T> Collection<T> getAll(Class<T> type) {
        Collection<T> result = new ArrayList<>();
        objects.forEach((k,v) -> {
            if (type.equals(k.getType())) {
                result.add((T)v);
            }
        });
        return result;
    }

    @Override
    public synchronized <T> void remove(CK<T> ck) {
        T oldData = (T)objects.remove(ck);
        if (oldData != null) {
            persistenceService.onNodeDeleted(oldData, ck);
        }
        Set<CK> remove = new HashSet<>();
        objects.forEach((k,v) -> {
            if(k.startsWith(ck)) {
                remove.add(k);
            }
        });
        remove.forEach(k -> {
                Object removed = objects.remove(k);
                persistenceService.onNodeDeleted(removed, k);
            }
        );
    }

    @Override
    public synchronized void apply(Collection<OperationsBuilder.Operation> operations) {
        for (OperationsBuilder.Operation operation: operations) {
            switch (operation.getType()) {
                case WRITE:
                    put(operation.getCk(), operation.getObject());
                    break;
                case DELETE:
                    remove(operation.getCk());
                    break;
                default:
                    throw new UnsupportedOperationException("Cache operation " + operation.getType() + " is not implemented !");
            }
        }
    }

}
