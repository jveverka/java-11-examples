package itx.examples.jetty.common.services;

import itx.examples.jetty.common.dto.ConversationId;
import itx.examples.jetty.common.dto.Message;

/**
 * Message listener for particular conversation group and user.
 */
public interface MessageListener {

    /**
     * get Id of this message listener
     * @return
     *   conversation Id of this listener
     */
    ConversationId getId();

    /**
     * triggered on message arrival into conversation group
     * @param message
     */
    void onMessage(Message message);

    /**
     * triggered if this conversation group is terminated
     * this listener will not get notifications after closed.
     */
    void onClose();

    /**
     * triggered on listener error
     * this listener will not get notifications after error occurred.
     * @param reason
     *  error description
     */
    void onError(String reason);

}
