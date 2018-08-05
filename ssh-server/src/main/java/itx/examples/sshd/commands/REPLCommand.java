package itx.examples.sshd.commands;

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

    public REPLCommand(CommandProcessor commandProcessor) {
        this.executorService = Executors.newSingleThreadExecutor();
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
        REPLCommandProcessor simpleCommandProcessor = new REPLCommandProcessor(commandProcessor, stdin, stdout, stderr, exitCallback);
        executorService.submit(simpleCommandProcessor);
    }

    @Override
    public void destroy() throws Exception {
        LOG.info("destroy");
        executorService.shutdown();
    }

}
