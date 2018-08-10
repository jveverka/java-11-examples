package itx.ssh.server.commands;

public interface CommandResult {

    int getReturnCode();

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
