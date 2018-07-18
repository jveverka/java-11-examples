package itx.examples.jetty.common.services;

import itx.examples.jetty.common.dto.Message;

import java.util.List;
import java.util.Optional;

/**
 * Message data services, this services provides chat-like
 * services for clients.
 * This interface contains only synchronous methods.
 */
public interface MessageServiceSync {

    /**
     * publish message to particular groupId
     * @param message
     *  message to be published
     * @return
     *  number of message listeners notified
     */
    Long publishMessage(Message message);

    /**
     * get all messages for particular group
     * @param groupId
     *  unique identifier of message group
     * @return
     *  list of messages for group, if not present groupId does not exist
     */
    Optional<List<Message>> getMessages(String groupId);

    /**
     * get all groups available within this message services
     * @return
     *  list of group Ids
     */
    List<String> getGroups();

    /**
     * delete all messages in particular group
     * and effectively remove group.
     * All listeners for this group will be closed.
     * @param groupId
     *   unique identifier of message group
     * @return
     *   number of messages being deleted, if not present groupId does not exist.
     */
    Optional<Long> cleanMessages(String groupId);

}
