package itx.ssh.server.commands.keymaps;

public interface KeyMap {

    byte getEnterKeyCode();

    byte getBackSpaceKeyCode();

    byte getSequencePrefix();

    boolean isKeyLeftSequence(byte... sequence);

    boolean isKeyRightSequence(byte... sequence);

    boolean isKeyDeleteSequence(byte... sequence);

    boolean isKeyHomeSequence(byte... sequence);

    boolean isKeyEndSequence(byte... sequence);

    byte[] getKeyLeftSequence();

    byte[] getKeyRightSequence();

    byte[] getKeyDeleteSequence();

    byte[] getKeyHomeSequence();

    byte[] getKeyEndSequence();

}
