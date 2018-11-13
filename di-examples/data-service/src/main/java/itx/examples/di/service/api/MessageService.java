package itx.examples.di.service.api;

import java.util.concurrent.Future;

public interface MessageService {

    Future<MessageResponse> getMessage(MessageRequest messageRequest);

    void failWithException() throws Exception;

}
