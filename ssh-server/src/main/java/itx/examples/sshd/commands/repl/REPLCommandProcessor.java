package itx.examples.sshd.commands.repl;

import itx.examples.sshd.commands.CommandProcessor;
import itx.examples.sshd.commands.keymaps.KeyMap;
import org.apache.sshd.server.ExitCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

public class REPLCommandProcessor implements Runnable {

    final private static Logger LOG = LoggerFactory.getLogger(REPLCommandProcessor.class);
    final private static String EMPTY = "";

    final private static String CMD_EXIT = "exit";

    private InputStream stdin;
    private OutputStream stdout;
    private OutputStream stderr;
    private ExitCallback exitCallback;
    private CommandProcessor commandProcessor;
    private KeyMap keyMap;

    public REPLCommandProcessor(KeyMap keyMap, CommandProcessor commandProcessor,
                                InputStream stdin, OutputStream stdout, OutputStream stderr, ExitCallback exitCallback) {
        this.stdin = stdin;
        this.stdout = stdout;
        this.stderr = stderr;
        this.exitCallback = exitCallback;
        this.commandProcessor = commandProcessor;
        this.keyMap = keyMap;
    }

    @Override
    public void run() {
        try {
            Reader r = new InputStreamReader(stdin, "UTF-8");
            int intch;
            String commandBuffer = EMPTY;
            String command = EMPTY;
            while ((intch = r.read()) != -1) {
                char ch = (char) intch;
                if (intch == keyMap.getEnterKeyCode()) {
                    stdout.write('\n');
                    stdout.write('\r');
                    command = commandBuffer;
                    commandBuffer = EMPTY;
                    stdout.flush();
                    processCommand(command);
                } else if (intch == keyMap.getBackSpaceKeyCode()) {
                    writeBlanks(stdout, commandBuffer);
                    commandBuffer = commandBuffer.substring(0, commandBuffer.length() -1);
                    stdout.write('\r');
                    stdout.write(commandBuffer.getBytes());
                    stdout.flush();
                } else {
                    commandBuffer = commandBuffer + ch;
                    stdout.write(ch);
                    stdout.flush();
                }
            }
        } catch (IOException e) {
            LOG.error("ERROR: ", e);
        }
    }

    private static void writeBlanks(OutputStream stdout, String command) throws IOException {
        stdout.write('\r');
        for (int i=0; i<command.length(); i++) {
            stdout.write(' ');
        }
    }

    private void processCommand(String command) throws IOException {
        command = command.trim();
        if (CMD_EXIT.equals(command)) {
            LOG.info("on exit");
            exitCallback.onExit(0);
        } else {
            commandProcessor.processCommand(command, stdout, stderr);
            stdout.flush();
            stderr.flush();
        }
    }

}
