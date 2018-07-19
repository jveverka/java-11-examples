package itx.futures.demo;

import com.google.common.util.concurrent.ListenableFuture;
import itx.futures.demo.dto.DataInput;
import itx.futures.demo.dto.DataResult;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

public interface DataProviderService extends AutoCloseable {

    /**
     * Classical method returning {@link Future} without possibility of registering listener.
     * @param dataInput input data for processing.
     * @return {@link Future} holding upcoming result or exception.
     */
    Future<DataResult> getData(DataInput dataInput);

    /**
     * More advanced method returning {@link CompletionStage} so client have possibility to add listener-like hooks.
     * @param dataInput input data for processing.
     * @return {@link CompletionStage} holding upcoming result or exception.
     */
    CompletionStage<DataResult> getDataStage(DataInput dataInput);

    /**
     * Method returning guava's {@link ListenableFuture} so client have possibility to add listener-like hooks.
     * @param dataInput input data for processing.
     * @return {@link ListenableFuture} holding upcoming result or exception.
     */
    ListenableFuture<DataResult> getDataListenable(DataInput dataInput);

}
