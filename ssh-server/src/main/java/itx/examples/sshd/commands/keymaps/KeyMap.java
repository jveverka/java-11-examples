package itx.examples.sshd.commands.keymaps;

public interface KeyMap {

    int getEnterKeyCode();

    int getBackSpaceKeyCode();

    int getArrowPrefix();

    boolean isKeyLeftSequence(int ... sequence);

    boolean isKeyRightSequence(int ... sequence);

    int[] getKeyLeftSequence();

    int[] getKeyRightSequence();

}
