package itx.examples.fasterxml.dto.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import itx.examples.fasterxml.dto.DataHolder;

public class RedDataHolderImpl implements DataHolder  {

    private final String data;
    private final String value;

    @JsonCreator
    public RedDataHolderImpl(@JsonProperty("data") String data,
                             @JsonProperty("value") String value) {
        this.data = data;
        this.value = value;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public Class<? extends DataHolder> getType() {
        return RedDataHolderImpl.class;
    }

    public String getValue() {
        return value;
    }

}
