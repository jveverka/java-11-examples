package itx.ssh.server.commands.repl;

import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.OutputWriter;
import itx.ssh.server.commands.keymaps.KeyMap;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class REPLCommand implements Command {

    final private static Logger LOG = LoggerFactory.getLogger(REPLCommand.class);

    private InputStream stdin;
    private OutputStream stdout;
    private OutputStream stderr;
    private ExitCallback exitCallback;
    private ExecutorService executorService;
    private CommandProcessor commandProcessor;
    private KeyMap keyMap;
    private String prompt;
    private SshClientSessionCounter sshClientSessionCounter;

    public REPLCommand(String prompt, KeyMap keyMap, CommandProcessor commandProcessor, SshClientSessionCounter sshClientSessionCounter) {
        this.executorService = Executors.newSingleThreadExecutor();
        this.commandProcessor = commandProcessor;
        this.keyMap = keyMap;
        this.prompt = prompt;
        this.sshClientSessionCounter = sshClientSessionCounter;
    }

    @Override
    public void setInputStream(InputStream stdin) {
        LOG.info("setInputStream");
        this.stdin = stdin;
    }

    @Override
    public void setOutputStream(OutputStream stdout) {
        LOG.info("setOutputStream");
        this.stdout = stdout;
    }

    @Override
    public void setErrorStream(OutputStream stderr) {
        LOG.info("setErrorStream");
        this.stderr = stderr;
    }

    @Override
    public void setExitCallback(ExitCallback callback) {
        LOG.info("setExitCallback");
        this.exitCallback = callback;
    }

    @Override
    public void start(Environment env) throws IOException {
        long sessionId = sshClientSessionCounter.getNewSessionId();
        OutputWriterImpl outputWriter = new OutputWriterImpl(stdout, stderr);
        commandProcessor.onSessionStart(sessionId, outputWriter);
        LOG.info("start REPL command processor with sessionId: {}", sessionId);
        REPLCommandProcessor replCommandProcessor = new REPLCommandProcessor(prompt, keyMap, commandProcessor,
                stdin, outputWriter, exitCallback);
        executorService.submit(replCommandProcessor);
    }

    @Override
    public void destroy() throws Exception {
        LOG.info("destroy");
        executorService.shutdown();
    }

}
