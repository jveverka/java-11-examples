package itx.ssh.server;

import itx.ssh.server.commands.singlecommand.CommandFactoryImpl;
import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.repl.ShellFactoryImpl;
import itx.ssh.server.commands.keymaps.KeyMap;
import itx.ssh.server.commands.keymaps.KeyMapProvider;
import itx.ssh.server.commands.subsystem.SshClientSession;
import itx.ssh.server.commands.subsystem.SshClientSessionListener;
import itx.ssh.server.commands.subsystem.SshClientNamedCommandFactory;
import itx.ssh.server.commands.subsystem.SshClientSessionCounter;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.command.Command;

import java.util.ArrayList;
import java.util.List;

public class SshServerBuilder {

    private final SshServer sshd;
    private final SshClientSessionCounter sshClientSessionCounter;
    private KeyMap keyMap;

    public SshServerBuilder() {
        this.sshd = SshServer.setUpDefaultServer();
        this.keyMap = KeyMapProvider.createDefaultKeyMap();
        this.sshClientSessionCounter = new SshClientSessionCounter();
    }

    public SshServerBuilder(SshServer sshd) {
        this.sshd = sshd;
        this.keyMap = KeyMapProvider.createDefaultKeyMap();
        this.sshClientSessionCounter = new SshClientSessionCounter();
    }

    public SshServerBuilder withKeyMap(KeyMap keyMap) {
        this.keyMap = keyMap;
        return this;
    }

    public SshServerBuilder withPasswordAuthenticator(PasswordAuthenticator passwordAuthenticator) {
        sshd.setPasswordAuthenticator(passwordAuthenticator);
        return this;
    }

    public SshServerBuilder withKeyPairProvider(KeyPairProvider keyPairProvider) {
        sshd.setKeyPairProvider(keyPairProvider);
        return this;
    }

    /**
     * Set prompt and {@link CommandProcessor} for REPL interface.
     * @param prompt implementation of {@link CommandProcessor} dedicated to REPL interface command processing.
     * @param commandProcessor
     * @return
     */
    public SshServerBuilder withShellFactory(String prompt, CommandProcessor commandProcessor) {
        sshd.setShellFactory(new ShellFactoryImpl(prompt, keyMap, commandProcessor, sshClientSessionCounter));
        return this;
    }

    /**
     * Set {@link CommandProcessor} for single command processing.
     * @param commandProcessor implementation of {@link CommandProcessor} dedicated to single command processing.
     * @return
     */
    public SshServerBuilder withCommandFactory(CommandProcessor commandProcessor) {
        sshd.setCommandFactory(new CommandFactoryImpl(commandProcessor, sshClientSessionCounter));
        return this;
    }

    /**
     * Set {@link CommandProcessor} for ssh-client library processing.
     * @param commandProcessor implementation of {@link CommandProcessor} dedicated to ssh-client communication.
     * @param sshClientSessionListener provides instances of {@link SshClientSession} for pushing data to ssh-client.
     * @return
     */
    public SshServerBuilder withSshClientProcessor(CommandProcessor commandProcessor,
                                                   SshClientSessionListener sshClientSessionListener) {
        List<NamedFactory<Command>> namedFactories = new ArrayList<>();
        namedFactories.add(new SshClientNamedCommandFactory(keyMap, commandProcessor,
                sshClientSessionListener, sshClientSessionCounter));
        sshd.setSubsystemFactories(namedFactories);
        return this;
    }

    public SshServerBuilder setPort(int port) {
        sshd.setPort(port);
        return this;
    }

    public SshServer build() {
        return sshd;
    }

}
