package itx.rxjava.dto;

public class DataItem {

    private final String data;
    private final Integer ordinal;

    public DataItem(String data, Integer ordinal) {
        this.data = data;
        this.ordinal = ordinal;
    }

    public String getData() {
        return data;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

}
