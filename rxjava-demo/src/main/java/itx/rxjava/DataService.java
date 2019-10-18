package itx.rxjava;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import itx.rxjava.dto.DataItem;
import itx.rxjava.dto.DataQuery;
import itx.rxjava.producer.CompletableDataItem;

public interface DataService {

    /**
     * This method returns data flow to client. Client thread is not blocked.
     * @param dataQuery query parameters for data flow.
     * @return consumable data flow with back pressure.
     */
    Flowable<DataItem> getDataFlowWithBackPressure(DataQuery dataQuery);

    /**
     * This method returns data flow to client. Client thread is not blocked.
     * @param dataQuery query parameters for data flow.
     * @return consumable data flow.
     */
    Observable<DataItem> getDataFlow(DataQuery dataQuery);

    Single<DataItem> getSingle(DataQuery dataQuery);

    CompletableDataItem getCompletable(DataQuery dataQuery);

    Maybe<DataItem> getMaybe(DataQuery dataQuery);

}
