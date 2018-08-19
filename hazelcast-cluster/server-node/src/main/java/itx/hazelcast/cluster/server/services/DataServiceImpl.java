package itx.hazelcast.cluster.server.services;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class DataServiceImpl implements DataService {

    private HazelcastInstance hazelcastInstance;
    private IMap<String, Object> dataServiceCache;

    public DataServiceImpl(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        this.dataServiceCache = hazelcastInstance.getMap("dataServiceCache");
    }

    @Override
    public void putData(String key, Object value) {
        dataServiceCache.put(key, value);
    }

    @Override
    public <T> T getValue(String key, Class<T> type) {
        Object o = dataServiceCache.get(key);
        return (T)o;
    }
}
