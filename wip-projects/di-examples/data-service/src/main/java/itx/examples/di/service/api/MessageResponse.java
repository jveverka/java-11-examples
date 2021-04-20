package itx.examples.di.service.api;

public class MessageResponse {

    private final String response;
    private final MessageRequest request;

    public MessageResponse(String response, MessageRequest request) {
        this.response = response;
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public MessageRequest getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return response;
    }
}
