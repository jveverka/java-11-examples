package itx.ssh.server.commands;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SingleCommand implements Command {

    final private static Logger LOG = LoggerFactory.getLogger(SingleCommand.class);

    private InputStream stdin;
    private OutputStream stdout;
    private OutputStream stderr;
    private ExitCallback exitCallback;
    private CommandProcessor commandProcessor;
    private byte[] command;

    public SingleCommand(byte[] command, CommandProcessor commandProcessor) {
        this.command = command;
        this.commandProcessor = commandProcessor;
    }

    @Override
    public void setInputStream(InputStream stdin) {
        this.stdin = stdin;
    }

    @Override
    public void setOutputStream(OutputStream stdout) {
        this.stdout = stdout;
    }

    @Override
    public void setErrorStream(OutputStream stderr) {
        this.stderr = stderr;
    }

    @Override
    public void setExitCallback(ExitCallback exitCallback) {
        this.exitCallback = exitCallback;
    }

    @Override
    public void start(Environment env) throws IOException {
        LOG.info("start");
        CommandResult commandResult = commandProcessor.processCommand(command, stdout, stderr);
        exitCallback.onExit(commandResult.getReturnCode());
    }

    @Override
    public void destroy() throws Exception {
        LOG.info("destroy");
    }

}
