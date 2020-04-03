package itx.examples.fasterxml.services;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import itx.examples.fasterxml.dto.DataHolder;

import java.util.Collection;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
public interface DataService {

    Collection<DataHolder> getData();

}
