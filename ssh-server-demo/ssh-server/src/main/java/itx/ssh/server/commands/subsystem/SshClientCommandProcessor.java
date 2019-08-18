package itx.ssh.server.commands.subsystem;

import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.CommandResult;
import itx.ssh.server.commands.keymaps.KeyMap;
import itx.ssh.server.utils.DataBuffer;
import itx.ssh.server.utils.OutputWriterImpl;
import org.apache.sshd.server.ExitCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class SshClientCommandProcessor implements Runnable {

    final private static Logger LOG = LoggerFactory.getLogger(SshClientCommandProcessor.class);

    private InputStream stdin;
    private OutputWriterImpl outputWriter;
    private ExitCallback exitCallback;
    private CommandProcessor commandProcessor;
    private KeyMap keyMap;
    private Charset charset;

    public SshClientCommandProcessor(InputStream stdin, OutputWriterImpl outputWriter,
                                     ExitCallback exitCallback, CommandProcessor commandProcessor, KeyMap keyMap) {
        this.stdin = stdin;
        this.outputWriter = outputWriter;
        this.exitCallback = exitCallback;
        this.commandProcessor = commandProcessor;
        this.keyMap = keyMap;
        this.charset = Charset.forName("UTF-8");
    }

    @Override
    public void run() {
        LOG.info("starting ssh-client command listener");
        try {
            Reader r = new InputStreamReader(stdin, charset);
            int intch;
            DataBuffer dataBuffer = new DataBuffer();
            while ((intch = r.read()) != -1) {
                if (intch == keyMap.getEnterKeyCode()) {
                    if (!processCommand(dataBuffer.getAndReset())) {
                        return;
                    }
                } else {
                    dataBuffer.add((byte)intch);
                }
            }
        } catch (IOException e) {
            LOG.error("ERROR: ", e);
        }
        LOG.info("ssh-client command listener terminated");
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
