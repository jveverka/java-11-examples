package itx.examples.sshd.commands;

import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.CommandResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class StringCommandProcessorImpl implements CommandProcessor {

    final private static Logger LOG = LoggerFactory.getLogger(StringCommandProcessorImpl.class);
    final private static String CMD_SET = "set";
    final private static String CMD_GET = "get";
    final private static String CMD_EXIT = "exit";

    private String state;

    public StringCommandProcessorImpl() {
        state = "";
    }

    @Override
    public CommandResult processCommand(byte[] command, OutputStream stdout, OutputStream stderr) throws IOException {
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
            stdout.write("state: ".getBytes());
            stdout.write(state.getBytes());
            stdout.write('\n');
            stdout.write('\r');
            stdout.flush();
            return CommandResult.ok();
        } else if (CMD_EXIT.equals(cmdElements[0].trim())) {
            LOG.info("exit");
            return CommandResult.terminateSessionOk();
        } else {
            LOG.info("unsupported command: {}", cmd);
            return CommandResult.from(255);
        }
    }

}
