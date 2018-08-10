package itx.ssh.server.commands;

import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandFactoryImpl implements CommandFactory {

    final private static Logger LOG = LoggerFactory.getLogger(CommandFactoryImpl.class);

    private CommandProcessor commandProcessor;

    public CommandFactoryImpl(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Command createCommand(String command) {
        LOG.info("createCommand: {}", command);
        return new SingleCommand(command, commandProcessor);
    }

}
