package itx.ssh.server.commands.keymaps;

public interface KeyMap {

    int getEnterKeyCode();

    int getBackSpaceKeyCode();

    int getSequencePrefix();

    boolean isKeyLeftSequence(int... sequence);

    boolean isKeyRightSequence(int... sequence);

    boolean isKeyDeleteSequence(int... sequence);

    boolean isKeyHomeSequence(int... sequence);

    boolean isKeyEndSequence(int... sequence);

    int[] getKeyLeftSequence();

    int[] getKeyRightSequence();

    int[] getKeyDeleteSequence();

    int[] getKeyHomeSequence();

    int[] getKeyEndSequence();

}
