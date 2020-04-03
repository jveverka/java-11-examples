package itx.examples.fasterxml.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "typeId")
public interface DataHolder {

    String getData();

    @JsonIgnore
    Class<? extends DataHolder> getType();

}
