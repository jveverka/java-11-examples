package itx.ssh.server.commands.subsystem;

import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.keymaps.KeyMap;
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

public class RobotCommand implements Command {

    final private static Logger LOG = LoggerFactory.getLogger(RobotCommand.class);

    private InputStream stdin;
    private OutputStream stdout;
    private OutputStream stderr;
    private ExitCallback exitCallback;
    private CommandProcessor commandProcessor;
    private KeyMap keyMap;
    private ExecutorService executorService;

    public RobotCommand(KeyMap keyMap, CommandProcessor commandProcessor) {
        this.keyMap = keyMap;
        this.commandProcessor = commandProcessor;
        this.executorService = Executors.newSingleThreadExecutor();
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
        RobotCommandProcessor robotCommandProcessor = new RobotCommandProcessor(stdin, stdout, stderr,
                exitCallback, commandProcessor, keyMap);
        executorService.submit(robotCommandProcessor);
    }

    @Override
    public void destroy() throws Exception {
        LOG.info("destroy");
        this.executorService.shutdown();
    }

}
