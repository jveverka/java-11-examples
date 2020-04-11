package itx.examples.springboot.demo.dto;

public class RequestInfo {

    private final String url;
    private final String queryString;

    public RequestInfo(String url, String queryString) {
        this.url = url;
        this.queryString = queryString;
    }

    public String getUrl() {
        return url;
    }

    public String getQueryString() {
        return queryString;
    }

}
