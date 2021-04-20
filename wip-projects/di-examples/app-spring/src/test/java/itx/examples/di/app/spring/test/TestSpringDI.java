package itx.examples.di.app.spring.test;

import itx.examples.di.app.spring.Main;
import itx.examples.di.app.spring.service.MessageClientService;
import itx.examples.di.service.api.MessageRequest;
import itx.examples.di.service.api.MessageResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Main.class })
public class TestSpringDI {

    @Autowired
    private MessageClientService messageClientService;

    @Test
    public void testDi() throws ExecutionException, InterruptedException {
        Assert.assertNotNull(messageClientService);
        Assert.assertNotNull(messageClientService.getMessageAsyncService());
        Assert.assertNotNull(messageClientService.getMessageSyncService());

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
