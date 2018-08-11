package itx.ssh.client.impl;

import itx.ssh.client.Message;
import itx.ssh.client.SshSession;
import itx.ssh.client.SshSessionListener;
import itx.ssh.server.commands.keymaps.KeyMap;
import org.apache.sshd.client.channel.ChannelSubsystem;
import org.apache.sshd.client.session.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SshSessionImpl implements SshSession {

    final private static Logger LOG = LoggerFactory.getLogger(SshSessionImpl.class);

    private final ClientSession session;
    private final ChannelSubsystem robotChannel;
    private final SshSessionListener listener;
    private final KeyMap keyMap;
    private final ExecutorService executorService;

    public SshSessionImpl(ClientSession session, ChannelSubsystem robotChannel, SshSessionListener listener, KeyMap keyMap) {
        this.session = session;
        this.robotChannel = robotChannel;
        this.listener = listener;
        this.keyMap = keyMap;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    protected void init() throws IOException {
        //start listening thread
        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                LOG.info("starting client listening thread");
                Reader r = new InputStreamReader(robotChannel.getInvertedOut());
                List<Integer> buffer = new ArrayList<>(256);
                try {
                    int ch;
                    while ((ch = r.read()) != -1) {
                        if (ch != keyMap.getEnterKeyCode()) {
                            buffer.add(Integer.valueOf(ch));
                        } else {
                            byte[] message = new byte[buffer.size()];
                            for (int i=0; i<buffer.size();i++) {
                                message[i] = buffer.get(i).byteValue();
                            }
                            listener.onServerEvent(Message.from(message));
                            buffer.clear();
                        }
                    }
                } catch (IOException e) {
                    listener.onSessionClosed();
                    LOG.error("Error: ", e);
                }
            }
        });
    }

    @Override
    public void send(Message request) throws IOException {
        robotChannel.getInvertedIn().write(request.getData());
        robotChannel.getInvertedIn().write(keyMap.getEnterKeyCode());
        robotChannel.getInvertedIn().flush();
    }

    @Override
    public void close() throws Exception {
        listener.onSessionClosed();
        executorService.shutdown();
        robotChannel.close();
        session.close();
    }
}
