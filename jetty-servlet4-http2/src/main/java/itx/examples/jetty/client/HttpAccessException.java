package itx.examples.jetty.client;

public class HttpAccessException extends RuntimeException {

    public HttpAccessException(Exception e) {
        super(e);
    }

}
