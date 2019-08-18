package itx.ssh.server.commands.singlecommand;

import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.CommandResult;
import itx.ssh.server.commands.OutputWriter;
import itx.ssh.server.commands.subsystem.SshClientSessionCounter;
import itx.ssh.server.utils.OutputWriterImpl;
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
    private SshClientSessionCounter sshClientSessionCounter;

    public SingleCommand(byte[] command, CommandProcessor commandProcessor,
                         SshClientSessionCounter sshClientSessionCounter) {
        this.command = command;
        this.commandProcessor = commandProcessor;
        this.sshClientSessionCounter = sshClientSessionCounter;
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
        long sessionId = sshClientSessionCounter.getNewSessionId();
        OutputWriter outputWriter = new OutputWriterImpl(stdout, stderr);
        commandProcessor.onSessionStart(sessionId, outputWriter);
        LOG.info("start single command processor with sessionId: {}", sessionId);
        CommandResult commandResult = commandProcessor.processCommand(command);
        exitCallback.onExit(commandResult.getReturnCode());
    }

    @Override
    public void destroy() throws Exception {
        LOG.info("destroy");
    }

}
