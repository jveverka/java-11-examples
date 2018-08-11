package itx.ssh.server.commands.repl;

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

public class REPLCommandProcessor implements Runnable {

    final private static Logger LOG = LoggerFactory.getLogger(REPLCommandProcessor.class);

    private InputStream stdin;
    private OutputStream stdout;
    private OutputStream stderr;
    private ExitCallback exitCallback;
    private CommandProcessor commandProcessor;
    private KeyMap keyMap;
    private String prompt;
    private Charset charset;

    public REPLCommandProcessor(String prompt, KeyMap keyMap, CommandProcessor commandProcessor,
                                InputStream stdin, OutputStream stdout, OutputStream stderr, ExitCallback exitCallback) {
        this.stdin = stdin;
        this.stdout = stdout;
        this.stderr = stderr;
        this.exitCallback = exitCallback;
        this.commandProcessor = commandProcessor;
        this.keyMap = keyMap;
        this.prompt = prompt;
        this.charset = Charset.forName("UTF-8");
    }

    @Override
    public void run() {
        LOG.info("starting REPL command listener");
        try {
            Reader r = new InputStreamReader(stdin, charset);
            int intch;
            CommandRenderer commandRenderer = new CommandRenderer();
            writePrompt();
            while ((intch = r.read()) != -1) {
                char ch = (char) intch;
                //process commands in 'REPL' mode (command line for human interaction)
                if (intch == keyMap.getEnterKeyCode()) {
                    //on ENTER
                    stdout.write('\n');
                    stdout.write('\r');
                    if (!processCommand(commandRenderer.getCommandAndReset())) {
                        return;
                    }
                    writePrompt();
                } else if (intch == keyMap.getBackSpaceKeyCode()) {
                    //on BACKSPACE
                    writeBlanks(commandRenderer.getCommand());
                    commandRenderer.onBackSpace();
                    renderCommandline(commandRenderer);
                    stdout.flush();
                } else if (intch == keyMap.getSequencePrefix()) {
                    //special keys handling
                    int key1 = r.read();
                    int key2 = r.read();
                    if (keyMap.isKeyLeftSequence(intch, key1, key2)) {
                        if (commandRenderer.getCursorPosition() > 0) {
                            flushKeySequence(intch, key1, key2);
                        }
                        commandRenderer.onKeyLeft();
                    } else if (keyMap.isKeyRightSequence(intch, key1, key2)) {
                        if (!commandRenderer.isCursorInEndLinePosition()) {
                            flushKeySequence(intch, key1, key2);
                        }
                        commandRenderer.onKeyRight();
                    } else if (keyMap.isKeyDeleteSequence(intch, key1, key2)) {
                        int key3 = r.read(); //consume last character
                        writeBlanks(commandRenderer.getCommand());
                        commandRenderer.onDeleteKey();
                        renderCommandline(commandRenderer);
                        stdout.flush();
                    } else if (keyMap.isKeyHomeSequence(intch, key1, key2)) {
                        commandRenderer.onKeyHome();
                        stdout.write('\r');
                        moveRight(prompt.length());
                        stdout.flush();
                    } else if (keyMap.isKeyEndSequence(intch, key1, key2)) {
                        commandRenderer.onKeyEnd();
                        stdout.write('\r');
                        moveRight(prompt.length() + commandRenderer.getCommand().length());
                        stdout.flush();
                    } else {
                        LOG.error("Unsupported key sequence {} {} {}", intch, key1, key2);
                    }
                } else {
                    //on normal character
                    commandRenderer.onCharInsert(ch);
                    renderCommandline(commandRenderer);
                    stdout.flush();
                }
            }
        } catch (IOException e) {
            LOG.error("ERROR: ", e);
        }
        LOG.info("REPL command listener terminated");
    }

    private void renderCommandline(CommandRenderer commandRenderer) throws IOException {
        stdout.write('\r');
        stdout.write((prompt + commandRenderer.getCommand()).getBytes(charset));
        stdout.write('\r');
        moveRight(prompt.length() + commandRenderer.getCursorPosition());
    }

    private void writePrompt() throws IOException {
        stdout.write(prompt.getBytes(charset));
        stdout.flush();
    }

    private void writeBlanks(String command) throws IOException {
        stdout.write('\r');
        stdout.write(prompt.getBytes(charset));
        for (int i=0; i<command.length(); i++) {
            stdout.write(' ');
        }
    }

    private void moveRight(int cursorPosition) throws IOException {
        for (int i=0; i<cursorPosition; i++) {
            int[] keyRightSequence = keyMap.getKeyRightSequence();
            for (int j=0; j<keyRightSequence.length; j++) {
                stdout.write(keyRightSequence[j]);
            }
        }
    }

    private void flushKeySequence(int key0, int key1, int key2) throws IOException {
        stdout.write(key0);
        stdout.write(key1);
        stdout.write(key2);
        stdout.flush();
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
