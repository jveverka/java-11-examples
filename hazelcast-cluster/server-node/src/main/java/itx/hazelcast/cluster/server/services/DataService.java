package itx.hazelcast.cluster.server.services;

public interface DataService {

    void putData(String key, Object value);

    <T> T getValue(String key, Class<T> type);

}
