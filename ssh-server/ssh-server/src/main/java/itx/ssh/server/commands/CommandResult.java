package itx.ssh.server.commands;

/**
 * Result of command processed by {@link CommandProcessor}.
 */
public interface CommandResult {

    /**
     * Return code of command.
     * @return
     */
    int getReturnCode();

    /**
     * Session termination flag. Some commands may terminate underlying ssh session, like for instance 'exit' or 'quit' command.
     * @return true if this command terminates underlying ssh session on server, false otherwise.
     */
    boolean terminateSession();

    static CommandResult from(int returnCode, boolean terminateSession) {
        return new CommandResult() {
            @Override
            public int getReturnCode() {
                return returnCode;
            }

            @Override
            public boolean terminateSession() {
                return terminateSession;
            }
        };
    }

    static CommandResult from(int returnCode) {
        return from(returnCode, false);
    }

    static CommandResult ok() {
        return from(0, false);
    }

    static CommandResult terminateSessionOk() {
        return from(0, true);
    }

    static CommandResult terminateSessionWithCode(int returnCode) {
        return from(returnCode, true);
    }

}
