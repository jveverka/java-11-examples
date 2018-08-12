package itx.ssh.server.commands.repl;

import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.CommandResult;
import itx.ssh.server.commands.keymaps.KeyMap;
import itx.ssh.server.utils.OutputWriterImpl;
import org.apache.sshd.server.ExitCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class REPLCommandProcessor implements Runnable {

    final private static Logger LOG = LoggerFactory.getLogger(REPLCommandProcessor.class);

    private InputStream stdin;
    private OutputWriterImpl outputWriter;
    private ExitCallback exitCallback;
    private CommandProcessor commandProcessor;
    private KeyMap keyMap;
    private String prompt;
    private Charset charset;

    public REPLCommandProcessor(String prompt, KeyMap keyMap, CommandProcessor commandProcessor,
                                InputStream stdin, OutputWriterImpl outputWriter, ExitCallback exitCallback) {
        this.stdin = stdin;
        this.outputWriter = outputWriter;
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
            CommandRenderer commandRenderer = new CommandRenderer();
            writePrompt();
            byte ch;
            while ((ch = (byte)r.read()) != -1) {
                //process commands in 'REPL' mode (command line for human interaction)
                if (ch == keyMap.getEnterKeyCode()) {
                    //on ENTER
                    outputWriter.writeStdOut((byte)'\n');
                    outputWriter.writeStdOut((byte)'\r');
                    if (!processCommand(commandRenderer.getCommandBytesAndReset())) {
                        return;
                    }
                    writePrompt();
                } else if (ch == keyMap.getBackSpaceKeyCode()) {
                    //on BACKSPACE
                    writeBlanks(commandRenderer.getCommand());
                    commandRenderer.onBackSpace();
                    renderCommandline(commandRenderer);
                    outputWriter.flushStdOut();
                } else if (ch == keyMap.getSequencePrefix()) {
                    //special keys handling
                    byte key1 = (byte)r.read();
                    byte key2 = (byte)r.read();
                    if (keyMap.isKeyLeftSequence(ch, key1, key2)) {
                        if (commandRenderer.getCursorPosition() > 0) {
                            flushKeySequence(ch, key1, key2);
                        }
                        commandRenderer.onKeyLeft();
                    } else if (keyMap.isKeyRightSequence(ch, key1, key2)) {
                        if (!commandRenderer.isCursorInEndLinePosition()) {
                            flushKeySequence(ch, key1, key2);
                        }
                        commandRenderer.onKeyRight();
                    } else if (keyMap.isKeyDeleteSequence(ch, key1, key2)) {
                        byte key3 = (byte)r.read(); //consume last character
                        writeBlanks(commandRenderer.getCommand());
                        commandRenderer.onDeleteKey();
                        renderCommandline(commandRenderer);
                        outputWriter.flushStdOut();
                    } else if (keyMap.isKeyHomeSequence(ch, key1, key2)) {
                        commandRenderer.onKeyHome();
                        outputWriter.writeStdOut((byte)'\r');
                        moveRight(prompt.length());
                        outputWriter.flushStdOut();
                    } else if (keyMap.isKeyEndSequence(ch, key1, key2)) {
                        commandRenderer.onKeyEnd();
                        outputWriter.writeStdOut((byte)'\r');
                        moveRight(prompt.length() + commandRenderer.getCommand().length());
                        outputWriter.flushStdOut();
                    } else {
                        LOG.error("Unsupported key sequence {} {} {}", ch, key1, key2);
                    }
                } else {
                    //on normal character
                    commandRenderer.onCharInsert(ch);
                    renderCommandline(commandRenderer);
                    outputWriter.flushStdOut();
                }
            }
        } catch (IOException e) {
            LOG.error("ERROR: ", e);
        }
        LOG.info("REPL command listener terminated");
    }

    private void renderCommandline(CommandRenderer commandRenderer) throws IOException {
        outputWriter.writeStdOut((byte)'\r');
        outputWriter.writeStdOut((prompt + commandRenderer.getCommand()).getBytes(charset));
        outputWriter.writeStdOut((byte)'\r');
        moveRight(prompt.length() + commandRenderer.getCursorPosition());
    }

    private void writePrompt() throws IOException {
        outputWriter.writeStdOut(prompt.getBytes(charset));
        outputWriter.flushStdOut();
    }

    private void writeBlanks(String command) throws IOException {
        outputWriter.writeStdOut((byte)'\r');
        outputWriter.writeStdOut(prompt.getBytes(charset));
        for (int i=0; i<command.length(); i++) {
            outputWriter.writeStdOut((byte)' ');
        }
    }

    private void moveRight(int cursorPosition) throws IOException {
        for (int i=0; i<cursorPosition; i++) {
            byte[] keyRightSequence = keyMap.getKeyRightSequence();
            for (int j=0; j<keyRightSequence.length; j++) {
                outputWriter.writeStdOut(keyRightSequence[j]);
            }
        }
    }

    private void flushKeySequence(byte key0, byte key1, byte key2) throws IOException {
        outputWriter.writeStdOut(key0);
        outputWriter.writeStdOut(key1);
        outputWriter.writeStdOut(key2);
        outputWriter.flushStdOut();
    }

    private boolean processCommand(byte[] command) throws IOException {
        CommandResult commandResult = commandProcessor.processCommand(command);
        if (!commandResult.terminateSession()) {
            outputWriter.flushStdOut();
            outputWriter.flushStdErr();
            return true;
        } else {
            LOG.info("on exit");
            exitCallback.onExit(0);
            return false;
        }
    }

}
