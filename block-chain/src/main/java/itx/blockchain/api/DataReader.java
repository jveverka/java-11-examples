package itx.blockchain.api;

import java.io.IOException;

/**
 * Data reader used to read data object instance from byte array.
 * @param <T>
 */
public interface DataReader<T> {

    /**
     * Class type for which this data reader is created.
     * @return
     */
    Class<T> forClass();

    /**
     * Create an instance of Data class from byte array.
     * @param data raw serialized instance of data class.
     * @return instance of data class.
     * @throws IOException thrown in case reading from byte array goes wrong.
     */
    T readData(byte[] data) throws IOException;

}
