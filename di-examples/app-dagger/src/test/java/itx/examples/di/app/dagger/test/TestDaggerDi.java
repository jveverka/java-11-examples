package itx.examples.di.app.dagger.test;

import itx.examples.di.app.dagger.Main;
import itx.examples.di.app.dagger.service.MessageClientService;
import itx.examples.di.service.api.MessageRequest;
import itx.examples.di.service.api.MessageResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestDaggerDi {

    @Test
    public void testDi() throws ExecutionException, InterruptedException {
        Main.main(null);
        MessageClientService messageClientService = Main.getMessageClientService();
        Assert.assertNotNull(messageClientService);
        Assert.assertNotNull(messageClientService.getMessageSyncService());
        Assert.assertNotNull(messageClientService.getMessageAsyncService());

        MessageRequest request = new MessageRequest("John");
        Future<MessageResponse> message = messageClientService.getMessageSyncService().getMessage(request);
        Assert.assertNotNull(message);
        Assert.assertNotNull(message.get());
        Assert.assertEquals(message.get().getResponse(), "Sync Hello John");

        message = messageClientService.getMessageAsyncService().getMessage(request);
        Assert.assertNotNull(message);
        Assert.assertNotNull(message.get());
        Assert.assertEquals(message.get().getResponse(), "Async Hello John");

        try {
            messageClientService.getMessageSyncService().failWithException();
            Assert.fail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
