package itx.examples.sshd.commands;

import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.CommandResult;
import itx.ssh.server.commands.OutputWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

public class StringCommandProcessorImpl implements CommandProcessor, AutoCloseable {

    final private static Logger LOG = LoggerFactory.getLogger(StringCommandProcessorImpl.class);
    final private static String CMD_SET = "set";
    final private static String CMD_GET = "get";
    final private static String CMD_EXIT = "exit";

    private String state;
    private long sessionId;
    private OutputWriter outputWriter;

    public StringCommandProcessorImpl() {
        state = "";
    }

    @Override
    public void onSessionStart(long sessionId, OutputWriter outputWriter) {
        this.sessionId = sessionId;
        this.outputWriter = outputWriter;
    }

    @Override
    public CommandResult processCommand(byte[] command) throws IOException {
        String cmd = new String(command, Charset.forName("UTF-8"));
        String[] cmdElements = cmd.split(" ");
        if (CMD_SET.equals(cmdElements[0].trim())) {
            LOG.info("command: {}", cmd);
            if (cmdElements.length > 1) {
                state = cmdElements[1].trim();
            } else {
                state = "";
            }
            return CommandResult.ok();
        } else if (CMD_GET.equals(cmdElements[0].trim())) {
            LOG.info("command: {}", cmd);
            StringBuilder sb = new StringBuilder();
            sb.append("state: ");
            sb.append(state.getBytes());
            sb.append('\n');
            sb.append('\r');
            outputWriter.writeStdOutAndFlush(sb.toString().getBytes(Charset.forName("UTF-8")));
            return CommandResult.ok();
        } else if (CMD_EXIT.equals(cmdElements[0].trim())) {
            LOG.info("exit");
            return CommandResult.terminateSessionOk();
        } else {
            LOG.info("unsupported command: {}", cmd);
            return CommandResult.from(255);
        }
    }

    @Override
    public void close() throws Exception {
    }

}
