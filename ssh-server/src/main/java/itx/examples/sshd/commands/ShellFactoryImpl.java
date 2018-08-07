package itx.examples.sshd.commands;

import itx.examples.sshd.commands.keymaps.KeyMap;
import itx.examples.sshd.commands.repl.REPLCommand;
import org.apache.sshd.common.Factory;
import org.apache.sshd.server.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShellFactoryImpl implements Factory<Command> {

    final private static Logger LOG = LoggerFactory.getLogger(ShellFactoryImpl.class);

    private CommandProcessor commandProcessor;
    private KeyMap keyMap;

    public ShellFactoryImpl(KeyMap keyMap, CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
        this.keyMap = keyMap;
    }

    @Override
    public Command get() {
        LOG.info("get command");
        return new REPLCommand(keyMap, commandProcessor);
    }

    @Override
    public Command create() {
        LOG.info("create command");
        return new REPLCommand(keyMap, commandProcessor);
    }

}
