package itx.ssh.client;

public interface SshSessionListener {

    void onServerEvent(Message serverEvent);

    void onSessionClosed();

}
