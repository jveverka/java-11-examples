package itx.examples.di.service.impl;

import itx.examples.di.service.api.MessageRequest;
import itx.examples.di.service.api.MessageResponse;
import itx.examples.di.service.api.MessageService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class MessageServiceSyncImpl implements MessageService {

    @Override
    public Future<MessageResponse> getMessage(MessageRequest messageRequest) {
        MessageResponse response = new MessageResponse("Sync Hello " + messageRequest.getMessage(), messageRequest);
        CompletableFuture<MessageResponse> result = new CompletableFuture<>();
        result.complete(response);
        return result;
    }

    @Override
    public void failWithException() throws Exception {
        throw new Exception("EXCEPTION");
    }

}
