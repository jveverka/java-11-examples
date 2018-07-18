package itx.examples.jetty.common.services;

import itx.examples.jetty.common.dto.ConversationId;

/**
 * Message data services, this services provides chat-like
 * services for clients.
 */
public interface MessageServiceAsync extends MessageServiceSync {

    /**
     * register listener for particular message group
     * @param listener
     *   instance of message listener
     * @return
     *   true if group existed, false otherwise
     */
    Boolean registerListener(MessageListener listener);

    /**
     * unregister listener from particular message group
     * @param conversationId
     *   conversation id of message listener to unregister
     * @return
     *   true if listener was registered, false otherwise
     */
    Boolean unregisterListener(ConversationId conversationId);

}
