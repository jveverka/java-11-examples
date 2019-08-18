package itx.ssh.server.commands.repl;

import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.keymaps.KeyMap;
import itx.ssh.server.commands.subsystem.SshClientSessionCounter;
import org.apache.sshd.common.Factory;
import org.apache.sshd.server.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShellFactoryImpl implements Factory<Command> {

    final private static Logger LOG = LoggerFactory.getLogger(ShellFactoryImpl.class);

    private final CommandProcessor commandProcessor;
    private final KeyMap keyMap;
    private final String prompt;
    private final SshClientSessionCounter sshClientSessionCounter;

    public ShellFactoryImpl(String prompt, KeyMap keyMap, CommandProcessor commandProcessor,
                            SshClientSessionCounter sshClientSessionCounter) {
        this.commandProcessor = commandProcessor;
        this.keyMap = keyMap;
        this.prompt = prompt;
        this.sshClientSessionCounter = sshClientSessionCounter;
    }

    @Override
    public Command get() {
        LOG.info("get command");
        return new REPLCommand(prompt, keyMap, commandProcessor, sshClientSessionCounter);
    }

    @Override
    public Command create() {
        LOG.info("create command");
        return new REPLCommand(prompt, keyMap, commandProcessor, sshClientSessionCounter);
    }

}
