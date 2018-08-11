package itx.ssh.client.impl;

import itx.ssh.client.SshSession;
import itx.ssh.client.Message;
import itx.ssh.client.SshSessionListener;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SshSessionImpl implements SshSession {

    private static final int ENTER = 13;

    private final ClientSession session;
    private final ClientChannel channel;
    private final SshSessionListener listener;
    private final ExecutorService executorService;

    public SshSessionImpl(ClientSession session, ClientChannel channel, SshSessionListener listener) {
        this.session = session;
        this.channel = channel;
        this.listener = listener;
        this.executorService = Executors.newSingleThreadExecutor();
        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                Reader r = new InputStreamReader(channel.getInvertedOut());
                List<Integer> buffer = new ArrayList<>(512);
                try {
                    int ch;
                    while ((ch = r.read()) != -1) {
                        if (ch != ENTER) {
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
                }
            }
        });
    }

    @Override
    public void send(Message request) throws IOException {
        channel.getInvertedIn().write(request.getData());
        channel.getInvertedIn().flush();
    }

    @Override
    public void close() throws Exception {
        listener.onSessionClosed();
        executorService.shutdown();
        channel.close();
        session.close();
    }

}
