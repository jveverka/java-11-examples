package itx.examples.jetty.common.dto;

public class ErrorMessage {

    private String message;
    private String exception;

    public ErrorMessage(String message) {
        this.message = message;
        this.exception = "";
    }

    public ErrorMessage(Exception e) {
        this.message = "";
        this.exception = e.getMessage();
    }

    public ErrorMessage(String message, Exception e) {
        this.message = message;
        this.exception = e.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public String getException() {
        return exception;
    }

}
