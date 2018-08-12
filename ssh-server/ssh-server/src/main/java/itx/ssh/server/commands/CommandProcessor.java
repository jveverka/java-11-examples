package itx.ssh.server.commands;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Implementation of this interface encapsulates command processing logic on ssh-server.
 */
public interface CommandProcessor {

    /**
     * Set client session ID for this command processor.
     * @param sessionId
     */
    void updateSessionId(long sessionId);

    /**
     * This method is called when new command is available.
     * @param command command data to be processed.
     * @param stdout standard output stream to write processing data in.
     * @param stderr standard error output stream to write processing data in.
     * @return {@link CommandResult} for ssh-server and return code of processed command.
     * @throws IOException
     */
    CommandResult processCommand(byte[] command, OutputStream stdout, OutputStream stderr) throws IOException;

}
