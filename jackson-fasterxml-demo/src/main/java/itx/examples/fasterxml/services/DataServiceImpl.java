package itx.examples.fasterxml.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import itx.examples.fasterxml.dto.DataHolder;
import itx.examples.fasterxml.dto.impl.GreenDataHolderImpl;
import itx.examples.fasterxml.dto.impl.RedDataHolderImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DataServiceImpl implements DataService {

    private final Map<Class<? extends DataHolder>, DataHolder> data;

    public DataServiceImpl() {
        this.data = new HashMap<>();
        this.data.put(GreenDataHolderImpl.class, new GreenDataHolderImpl("data-green", "green"));
        this.data.put(RedDataHolderImpl.class, new RedDataHolderImpl("data-red", "red"));
    }

    @JsonCreator
    public DataServiceImpl(@JsonProperty("data") Collection<DataHolder> data) {
        this.data = new HashMap<>();
        data.forEach(d->{
            this.data.put(d.getType(), d);
        });
    }

    @Override
    public Collection<DataHolder> getData() {
        return data.values();
    }

}
