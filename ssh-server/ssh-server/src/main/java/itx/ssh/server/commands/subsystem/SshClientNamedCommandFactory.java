package itx.ssh.server.commands.subsystem;

import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.keymaps.KeyMap;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SshClientNamedCommandFactory implements NamedFactory<Command> {

    final private static Logger LOG = LoggerFactory.getLogger(SshClientNamedCommandFactory.class);

    private final KeyMap keyMap;
    private final CommandProcessor commandProcessor;
    private final SshClientSessionListener sshClientMessageDispatcherRegistration;
    private final SshClientSessionCounter sshClientSessionCounter;

    public SshClientNamedCommandFactory(KeyMap keyMap, CommandProcessor commandProcessor,
                                        SshClientSessionListener sshClientMessageDispatcherRegistration,
                                        SshClientSessionCounter sshClientSessionCounter) {
        this.keyMap = keyMap;
        this.commandProcessor = commandProcessor;
        this.sshClientMessageDispatcherRegistration = sshClientMessageDispatcherRegistration;
        this.sshClientSessionCounter = sshClientSessionCounter;
    }

    @Override
    public Command create() {
        LOG.info("create ssh-client command processor");
        return new SshClientCommand(keyMap, commandProcessor, sshClientMessageDispatcherRegistration, sshClientSessionCounter);
    }

    @Override
    public String getName() {
        return "ssh-client";
    }

}
