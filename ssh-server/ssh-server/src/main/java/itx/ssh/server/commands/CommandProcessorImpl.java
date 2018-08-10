package itx.ssh.server.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class CommandProcessorImpl implements CommandProcessor {

    final private static Logger LOG = LoggerFactory.getLogger(CommandProcessorImpl.class);
    final private static String CMD_SET = "set";
    final private static String CMD_GET = "get";

    private String state;

    public CommandProcessorImpl() {
        state = "";
    }

    @Override
    public int processCommand(String command, OutputStream stdout, OutputStream stderr) throws IOException {
        String[] cmdElements = command.split(" ");
        if (CMD_SET.equals(cmdElements[0].trim())) {
            LOG.info("command: {}", command);
            if (cmdElements.length > 1) {
                state = cmdElements[1].trim();
            } else {
                state = "";
            }
            return 0;
        } else if (CMD_GET.equals(cmdElements[0].trim())) {
            LOG.info("command: {}", command);
            stdout.write("state: ".getBytes());
            stdout.write(state.getBytes());
            stdout.write('\n');
            stdout.write('\r');
            stdout.flush();
            return 0;
        } else {
            LOG.info("unsupported command: {}", command);
            return 255;
        }
    }

}
