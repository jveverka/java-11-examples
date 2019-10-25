package itx.rxjava;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import itx.rxjava.dto.DataItem;
import itx.rxjava.dto.DataQuery;
import itx.rxjava.dto.SingleDataQuery;
import itx.rxjava.producer.CompletableDataItem;

public interface DataService {

    /**
     * This method returns data flow to client. Client thread is not blocked.
     * @param dataQuery query parameters for data flow.
     * @return {@link Flowable} consumable data flow with back pressure.
     */
    Flowable<DataItem> getDataFlowWithBackPressure(DataQuery dataQuery);

    /**
     * This method returns data flow to client. Client thread is not blocked.
     * @param dataQuery query parameters for data flow.
     * @return {@link Observable} consumable data flow.
     */
    Observable<DataItem> getDataFlow(DataQuery dataQuery);

    /**
     * This method returns single result.
     * @param dataQuery query parameters for single result.
     * @return {@link Single} consumable.
     */
    Single<DataItem> getSingle(SingleDataQuery dataQuery);

    /**
     * This method notifies client that task has been completed or has failed.
     * @param dataQuery query parameters for completable task;
     * @return {@link Completable}.
     */
    CompletableDataItem getCompletable(SingleDataQuery dataQuery);

    /**
     * This method returns single result.
     * @param dataQuery query parameters for completable task;
     * @return {@link Maybe} consumable.
     */
    Maybe<DataItem> getMaybe(SingleDataQuery dataQuery);

}
