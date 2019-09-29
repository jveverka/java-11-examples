package itx.elastic.demo;

import itx.elastic.demo.dto.DataQuery;
import itx.elastic.demo.dto.EventData;
import itx.elastic.demo.dto.EventId;

import java.util.Collection;
import java.util.Optional;

public interface DataService {

    void saveData(EventData eventData);

    Optional<EventData> getDataById(EventId id);

    void deleteData(EventData eventData);

    void deleteAll();

    void deleteData(Collection<EventId> ids);

    Collection<EventData> getData(DataQuery dataQuery);

}
