package itx.blockchain.api;

import java.io.IOException;

/**
 * Data writer used to write data object instance into byte array.
 * @param <T>
 */
public interface DataWriter<T> {

    /**
     * Class type for which this data writer is created.
     * @return
     */
    Class<T> forClass();

    /**
     * Write an instance of Data class into byte array.
     * @param data instance of data class.
     * @return serialized data class as byte array.
     * @throws IOException thrown in case writing into byte array goes wrong.
     */
    byte[] writeData(T data) throws IOException;

}
