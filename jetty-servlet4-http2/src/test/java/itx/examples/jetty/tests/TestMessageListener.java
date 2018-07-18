package itx.examples.jetty.tests;

import itx.examples.jetty.common.dto.ConversationId;
import itx.examples.jetty.common.dto.Message;
import itx.examples.jetty.common.services.MessageListener;

import java.util.ArrayList;
import java.util.List;

public class TestMessageListener implements MessageListener {

    private ConversationId conversationId;
    private List<Message> messages;
    private boolean terminated;
    private String error;

    public TestMessageListener(ConversationId conversationId) {
        this.conversationId = conversationId;
        this.messages = new ArrayList<>();
        this.terminated = false;
        this.error = null;
    }

    @Override
    public ConversationId getId() {
        return conversationId;
    }

    @Override
    public void onMessage(Message message) {
        messages.add(message);
    }

    @Override
    public void onClose() {
        terminated = true;
    }

    @Override
    public void onError(String reason) {

    }

    public List<Message> getMessages() {
        return messages;
    }

    public boolean isTerminated() {
        return terminated;
    }

    public String getError() {
        return error;
    }

    public boolean hasError() {
        return (error != null);
    }

}
