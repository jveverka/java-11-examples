package itx.ssh.server.commands.subsystem;

import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.keymaps.KeyMap;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamedCommandFactory implements NamedFactory<Command> {

    final private static Logger LOG = LoggerFactory.getLogger(NamedCommandFactory.class);

    private KeyMap keyMap;
    private CommandProcessor commandProcessor;

    public NamedCommandFactory(KeyMap keyMap, CommandProcessor commandProcessor) {
        this.keyMap = keyMap;
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Command create() {
        LOG.info("create robot command processor");
        return new RobotCommand(keyMap, commandProcessor);
    }

    @Override
    public String getName() {
        return "robot";
    }

}
