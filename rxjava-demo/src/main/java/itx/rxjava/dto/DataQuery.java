package itx.rxjava.dto;

public class DataQuery {

    private final String data;
    private final Integer results;

    public DataQuery(String data, Integer results) {
        this.data = data;
        this.results = results;
    }

    public String getData() {
        return data;
    }

    public Integer getResults() {
        return results;
    }

}
