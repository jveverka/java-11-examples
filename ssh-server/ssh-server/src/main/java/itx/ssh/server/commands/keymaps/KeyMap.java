package itx.ssh.server.commands.keymaps;

public interface KeyMap {

    int getEnterKeyCode();

    int getBackSpaceKeyCode();

    int getSequencePrefix();

    boolean isKeyLeftSequence(int... sequence);

    boolean isKeyRightSequence(int... sequence);

    boolean isKeyDeleteSequence(int... sequence);

    int[] getKeyLeftSequence();

    int[] getKeyRightSequence();

    int[] getDeleteSequence();

}
