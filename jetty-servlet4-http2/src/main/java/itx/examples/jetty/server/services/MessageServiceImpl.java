package itx.examples.jetty.server.services;

import itx.examples.jetty.common.dto.ConversationId;
import itx.examples.jetty.common.dto.Message;
import itx.examples.jetty.common.services.MessageListener;
import itx.examples.jetty.common.services.MessageServiceAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MessageServiceImpl implements MessageServiceAsync {

    final private static Logger LOG = LoggerFactory.getLogger(MessageServiceImpl.class);

    final private Map<String, List<Message>> messages; //indexed by group ID
    final private Map<ConversationId, MessageListener> listeners;

    public MessageServiceImpl() {
        LOG.info("service init ...");
        this.messages = new ConcurrentHashMap<>();
        this.listeners = new ConcurrentHashMap<>();
    }

    @Override
    public Long publishMessage(Message message) {
        String groupId = message.getId().getConversationId().getGroupId();
        List<Message> messagesForGroup = messages.get(groupId);
        if (messagesForGroup == null) {
            messagesForGroup = new ArrayList<>();
            messages.put(groupId, messagesForGroup);
        }
        messagesForGroup.add(message);
        return listeners.values().stream()
                 .filter(l -> l.getId().equals(groupId))
                 .peek(l -> l.onMessage(message))
                 .count();
    }

    @Override
    public Optional<List<Message>> getMessages(String groupId) {
        List<Message> messagesForGroup = messages.get(groupId);
        if (messagesForGroup != null) {
            return Optional.of(messagesForGroup);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<String> getGroups() {
        return new ArrayList<>(messages.keySet());
    }

    @Override
    public Optional<Long> cleanMessages(String groupId) {
        List<Message> messagesForGroup = messages.remove(groupId);
        if (messagesForGroup != null) {
            List<MessageListener> listenersToRemove = listeners.values().stream()
                    .filter(l -> l.getId().equals(groupId))
                    .peek(l -> l.onClose())
                    .collect(Collectors.toList());
            listenersToRemove.forEach(l -> { listeners.remove(l.getId()); });
            return Optional.of(Long.valueOf(messagesForGroup.size()));
        }
        return Optional.empty();
    }

    @Override
    public Boolean registerListener(MessageListener listener) {
        if (messages.get(listener.getId().getGroupId()) == null) {
            listener.onError("GroupId does not exist !");
        };
        return (listeners.put(listener.getId(), listener) == null);
    }

    @Override
    public Boolean unregisterListener(ConversationId conversationId) {
        return (listeners.remove(conversationId) != null);
    }

}
