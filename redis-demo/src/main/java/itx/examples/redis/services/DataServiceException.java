package itx.examples.redis.services;

public class DataServiceException extends Exception {

    public DataServiceException() {
    }

    public DataServiceException(Throwable t) {
        super(t);
    }

    public DataServiceException(String message) {
        super(message);
    }

}
