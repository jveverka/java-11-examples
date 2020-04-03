package itx.examples.fasterxml.dto.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import itx.examples.fasterxml.dto.DataHolder;

public class GreenDataHolderImpl implements DataHolder {

    private final String data;
    private final String name;

    @JsonCreator
    public GreenDataHolderImpl(@JsonProperty("data") String data,
                               @JsonProperty("name") String name) {
        this.data = data;
        this.name = name;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public Class<? extends DataHolder> getType() {
        return GreenDataHolderImpl.class;
    }

    public String getName() {
        return name;
    }

}
