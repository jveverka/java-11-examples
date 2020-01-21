package itx.examples.jetty.tests;

import itx.examples.jetty.common.dto.Message;
import itx.examples.jetty.common.MessageUtils;
import itx.examples.jetty.common.services.MessageServiceAsync;
import itx.examples.jetty.server.services.MessageServiceImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MassageServiceTests {

    @Test
    public void testInitializedService() {
        String groupId = "group01";
        MessageServiceAsync messageService = new MessageServiceImpl();

        List<String> groups = messageService.getGroups();
        Assert.assertNotNull(groups);
        Assert.assertTrue(groups.size() == 0);
        Optional<List<Message>> messages = messageService.getMessages(groupId);
        Assert.assertTrue(!messages.isPresent());
        Optional<Long> group01 = messageService.cleanMessages(groupId);
        Assert.assertTrue(!group01.isPresent());
    }

    @Test
    public void testSyncService() {
        String groupId01 = "group01";
        String userId01 = "user01";
        MessageServiceAsync messageService = new MessageServiceImpl();
        Long published = null;
        Optional<List<Message>> messages = null;
        Message message = null;
        Optional<Long> deletedMessages = null;
        List<Message> publishedMessages = new ArrayList<>();

        message = MessageUtils.createMessage(userId01, groupId01, "message01");
        publishedMessages.add(message);
        published = messageService.publishMessage(message);
        Assert.assertNotNull(published);
        Assert.assertTrue(published.longValue() == 0);

        message = MessageUtils.createMessage(userId01, groupId01, "message02");
        publishedMessages.add(message);
        published = messageService.publishMessage(message);
        Assert.assertNotNull(published);
        Assert.assertTrue(published.longValue() == 0);

        messages = messageService.getMessages(groupId01);
        Assert.assertNotNull(messages);
        Assert.assertTrue(messages.isPresent());
        Assert.assertTrue(messages.get().size() == 2);

        for (int i=0; i<publishedMessages.size(); i++) {
            Assert.assertTrue(messages.get().get(i).equals(publishedMessages.get(i)));
        }

        deletedMessages = messageService.cleanMessages(groupId01);
        Assert.assertNotNull(deletedMessages);
        Assert.assertTrue(deletedMessages.isPresent());
        Assert.assertEquals(deletedMessages.get(), Long.valueOf(2L));

        deletedMessages = messageService.cleanMessages(groupId01);
        Assert.assertNotNull(deletedMessages);
        Assert.assertTrue(!deletedMessages.isPresent());

    }

}
