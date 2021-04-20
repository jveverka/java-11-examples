package itx.examples.di.service.impl;

import itx.examples.di.service.api.MessageRequest;
import itx.examples.di.service.api.MessageResponse;
import itx.examples.di.service.api.MessageService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class MessageServiceAsyncImpl implements MessageService {

    private final ExecutorService executor;

    public MessageServiceAsyncImpl(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public Future<MessageResponse> getMessage(MessageRequest messageRequest) {
        return this.executor.submit(new MessageProcessingTask(messageRequest));
    }

    @Override
    public void failWithException() throws Exception {
        throw new Exception("EXCEPTION");
    }

}
