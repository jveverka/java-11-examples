package itx.ssh.server.commands;

import java.io.IOException;
import java.io.OutputStream;

public interface CommandProcessor {

    CommandResult processCommand(byte[] command, OutputStream stdout, OutputStream stderr) throws IOException;

}
