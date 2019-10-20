package itx.examples.jdbc.dto;

public interface DataRecordProcessor<T> {

    Class<T> getType();

    String getRecordName();

    byte[] serializeRecord(T data);

    T deserializeRecord(byte[] data);

}
