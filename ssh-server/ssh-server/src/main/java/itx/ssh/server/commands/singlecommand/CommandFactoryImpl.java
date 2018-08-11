package itx.ssh.server.commands.singlecommand;

import itx.ssh.server.commands.CommandProcessor;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class CommandFactoryImpl implements CommandFactory {

    final private static Logger LOG = LoggerFactory.getLogger(CommandFactoryImpl.class);

    private CommandProcessor commandProcessor;

    public CommandFactoryImpl(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Command createCommand(String command) {
        LOG.info("createCommand: {}", command);
        return new SingleCommand(command.getBytes(Charset.forName("UTF-8")), commandProcessor);
    }

}
