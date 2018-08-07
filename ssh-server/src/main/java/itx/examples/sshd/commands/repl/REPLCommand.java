package itx.examples.sshd.commands.repl;

import itx.examples.sshd.commands.CommandProcessor;
import itx.examples.sshd.commands.keymaps.KeyMap;
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

    public REPLCommand(String prompt, KeyMap keyMap, CommandProcessor commandProcessor) {
        this.executorService = Executors.newSingleThreadExecutor();
        this.commandProcessor = commandProcessor;
        this.keyMap = keyMap;
        this.prompt = prompt;
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
        LOG.info("start");
        REPLCommandProcessor simpleCommandProcessor = new REPLCommandProcessor(prompt, keyMap, commandProcessor,
                stdin, stdout, stderr, exitCallback);
        executorService.submit(simpleCommandProcessor);
    }

    @Override
    public void destroy() throws Exception {
        LOG.info("destroy");
        executorService.shutdown();
    }

}
