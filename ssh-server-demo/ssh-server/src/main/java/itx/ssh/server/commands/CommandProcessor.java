package itx.ssh.server.commands;

import java.io.IOException;

/**
 * Implementation of this interface encapsulates command processing logic on ssh-server.
 */
public interface CommandProcessor {

    /**
     * Set client session ID for this command processor.
     * Commands are processed after the session has started.
     * @param outputWriter interface for writing output data
     * @param sessionId
     */
    void onSessionStart(long sessionId, OutputWriter outputWriter);

    /**
     * This method is called when new command is available. It is called after session has started.
     * @param command command data to be processed.
     * @return {@link CommandResult} for ssh-server and return code of processed command.
     * @throws IOException
     */
    CommandResult processCommand(byte[] command) throws IOException;

}
