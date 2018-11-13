package itx.examples.di.service.api;

public class MessageRequest {

    private final String message;

    public MessageRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
