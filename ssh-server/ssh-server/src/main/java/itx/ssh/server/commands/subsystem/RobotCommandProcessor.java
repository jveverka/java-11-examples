package itx.ssh.server.commands.subsystem;

import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.CommandResult;
import itx.ssh.server.commands.keymaps.KeyMap;
import org.apache.sshd.server.ExitCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RobotCommandProcessor implements Runnable {

    final private static Logger LOG = LoggerFactory.getLogger(RobotCommandProcessor.class);

    private InputStream stdin;
    private OutputStream stdout;
    private OutputStream stderr;
    private ExitCallback exitCallback;
    private CommandProcessor commandProcessor;
    private KeyMap keyMap;
    private Charset charset;

    public RobotCommandProcessor(InputStream stdin, OutputStream stdout, OutputStream stderr,
                                 ExitCallback exitCallback, CommandProcessor commandProcessor, KeyMap keyMap) {
        this.stdin = stdin;
        this.stdout = stdout;
        this.stderr = stderr;
        this.exitCallback = exitCallback;
        this.commandProcessor = commandProcessor;
        this.keyMap = keyMap;
        this.charset = Charset.forName("UTF-8");
    }

    @Override
    public void run() {
        LOG.info("starting robot command listener");
        try {
            Reader r = new InputStreamReader(stdin, charset);
            int intch;
            List<Integer> buffer = new ArrayList<>(256);
            while ((intch = r.read()) != -1) {
                if (intch == keyMap.getEnterKeyCode()) {
                    char[] command = new char[buffer.size()];
                    for (int i = 0; i < buffer.size(); i++) {
                        command[i] = (char) buffer.get(i).intValue();
                    }
                    buffer.clear();
                    if (!processCommand(String.copyValueOf(command))) {
                        return;
                    }
                } else {
                    buffer.add(Integer.valueOf(intch));
                }
            }
        } catch (IOException e) {
            LOG.error("ERROR: ", e);
        }
        LOG.info("robot command listener terminated");
    }

    private boolean processCommand(String command) throws IOException {
        CommandResult commandResult = commandProcessor.processCommand(command, stdout, stderr);
        if (!commandResult.terminateSession()) {
            stdout.flush();
            stderr.flush();
            return true;
        } else {
            LOG.info("on exit");
            exitCallback.onExit(0);
            return false;
        }
    }

}
