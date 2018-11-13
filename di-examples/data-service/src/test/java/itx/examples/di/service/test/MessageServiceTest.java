package itx.examples.di.service.test;

import itx.examples.di.service.api.MessageRequest;
import itx.examples.di.service.api.MessageResponse;
import itx.examples.di.service.api.MessageService;
import itx.examples.di.service.impl.MessageServiceAsyncImpl;
import itx.examples.di.service.impl.MessageServiceSyncImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MessageServiceTest {

    @Test
    public void testSyncService() throws ExecutionException, InterruptedException {
        MessageRequest messageRequest = new MessageRequest("Tom");
        MessageService messageService = new MessageServiceSyncImpl();
        Future<MessageResponse> response = messageService.getMessage(messageRequest);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.get());
        Assert.assertEquals(response.get().getResponse(), "Sync Hello " + messageRequest.getMessage());
    }


    @Test
    public void testAsyncService() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        MessageRequest messageRequest = new MessageRequest("Jakub");
        MessageService messageService = new MessageServiceAsyncImpl(executor);
        Future<MessageResponse> response = messageService.getMessage(messageRequest);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.get());
        Assert.assertEquals(response.get().getResponse(), "Async Hello " + messageRequest.getMessage());
    }

}
