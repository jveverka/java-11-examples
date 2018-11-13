package itx.examples.di.service.impl;

import itx.examples.di.service.api.MessageRequest;
import itx.examples.di.service.api.MessageResponse;

import java.util.concurrent.Callable;

public class MessageProcessingTask implements Callable<MessageResponse> {

    private final MessageRequest request;

    public MessageProcessingTask(MessageRequest request) {
        this.request = request;
    }

    @Override
    public MessageResponse call() throws Exception {
        MessageResponse response = new MessageResponse("Async Hello " + request.getMessage(), request);
        return response;
    }

}
