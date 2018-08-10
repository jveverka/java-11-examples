package itx.ssh.server;

import itx.ssh.server.commands.CommandFactoryImpl;
import itx.ssh.server.commands.CommandProcessor;
import itx.ssh.server.commands.ShellFactoryImpl;
import itx.ssh.server.commands.keymaps.KeyMap;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;

public class SshServerBuilder {

    private final SshServer sshd;

    public SshServerBuilder() {
        sshd = SshServer.setUpDefaultServer();
    }

    public SshServerBuilder(SshServer sshd) {
        this.sshd = sshd;
    }

    public SshServerBuilder withPasswordAuthenticator(PasswordAuthenticator passwordAuthenticator) {
        sshd.setPasswordAuthenticator(passwordAuthenticator);
        return this;
    }

    public SshServerBuilder withKeyPairProvider(KeyPairProvider keyPairProvider) {
        sshd.setKeyPairProvider(keyPairProvider);
        return this;
    }

    public SshServerBuilder withShellFactory(String prompt, KeyMap keyMap, CommandProcessor commandProcessor) {
        sshd.setShellFactory(new ShellFactoryImpl(prompt, keyMap, commandProcessor));
        return this;
    }

    public SshServerBuilder withCommandFactory(CommandProcessor commandProcessor) {
        sshd.setCommandFactory(new CommandFactoryImpl(commandProcessor));
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
