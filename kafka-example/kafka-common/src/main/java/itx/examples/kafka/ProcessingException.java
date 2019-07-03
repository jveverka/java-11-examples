package itx.examples.kafka;

public class ProcessingException extends Exception {

    public ProcessingException(Exception e) {
        super(e);
    }

}
