package itx.ssh.client;

public interface SshSessionListener {

    void onServerEvent(byte[] serverEvent);

    void onSessionClosed();

}
