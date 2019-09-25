package itx.rxjava;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import itx.rxjava.dto.DataItem;
import itx.rxjava.dto.DataQuery;

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

}
