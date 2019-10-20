package itx.examples.jdbc.dto;

public interface DataRecord<T> {

    long getTimestamp();

    T getData();

}
