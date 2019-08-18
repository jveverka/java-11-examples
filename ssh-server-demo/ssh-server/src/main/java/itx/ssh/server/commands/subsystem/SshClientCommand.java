package itx.ssh.server.commands.subsystem;

import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.OutputWriter;
import itx.ssh.server.commands.keymaps.KeyMap;
import itx.ssh.server.utils.OutputWriterImpl;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SshClientCommand implements Command {

    final private static Logger LOG = LoggerFactory.getLogger(SshClientCommand.class);

    private InputStream stdin;
    private OutputStream stdout;
    private OutputStream stderr;
    private ExitCallback exitCallback;
    private CommandProcessor commandProcessor;
    private KeyMap keyMap;
    private ExecutorService executorService;
    private SshClientSessionListener sshClientMessageDispatcherRegistration;
    private SshClientSessionCounter sshClientSessionCounter;

    public SshClientCommand(KeyMap keyMap, CommandProcessor commandProcessor,
                            SshClientSessionListener sshClientMessageDispatcherRegistration,
                            SshClientSessionCounter sshClientSessionCounter) {
        this.keyMap = keyMap;
        this.commandProcessor = commandProcessor;
        this.executorService = Executors.newSingleThreadExecutor();
        this.sshClientMessageDispatcherRegistration = sshClientMessageDispatcherRegistration;
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
        OutputWriterImpl outputWriter = new OutputWriterImpl(stdout, stderr);
        commandProcessor.onSessionStart(sessionId, outputWriter);
        LOG.info("start ssh-client command processor with sessionId: {}", sessionId);
        SshClientSession sshClientSession =
                new SshClientSessionImpl(sessionId, stdout, exitCallback);
        SshClientCommandProcessor robotCommandProcessor = new SshClientCommandProcessor(stdin, outputWriter,
                exitCallback, commandProcessor, keyMap);
        executorService.submit(robotCommandProcessor);
        sshClientMessageDispatcherRegistration.onNewSession(sshClientSession);
    }

    @Override
    public void destroy() throws Exception {
        LOG.info("destroy");
        this.executorService.shutdown();
    }

}
