package itx.examples.sshd.commands;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShellFactoryImpl implements Factory<Command> {

    final private static Logger LOG = LoggerFactory.getLogger(ShellFactoryImpl.class);

    @Override
    public Command get() {
        LOG.info("get command");
        return new SimpleCommand();
    }

    @Override
    public Command create() {
        LOG.info("create command");
        return new SimpleCommand();
    }

}
