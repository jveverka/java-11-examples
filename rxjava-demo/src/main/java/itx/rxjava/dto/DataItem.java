package itx.rxjava.dto;

public class DataItem {

    private final String request;
    private final String result;
    private final Integer ordinal;

    public DataItem(String request, String result, Integer ordinal) {
        this.request = request;
        this.result = result;
        this.ordinal = ordinal;
    }

    public String getRequest() {
        return request;
    }

    public String getResult() {
        return result;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

}
