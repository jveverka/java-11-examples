package itx.examples.sshd.commands.keymaps;

public class DefaultKeyMap implements KeyMap {

    final private static int ENTER = 13;
    final private static int BACKSPACE = 127;
    final private static int ARROW_PREFIX = 27;

    @Override
    public int getEnterKeyCode() {
        return ENTER;
    }

    @Override
    public int getBackSpaceKeyCode() {
        return BACKSPACE;
    }

    @Override
    public int getArrowPrefix() {
        return ARROW_PREFIX;
    }

    @Override
    public boolean isKeyLeftSequence(int... sequence) {
        return sequence != null && sequence.length >= 3 && sequence[0] == ARROW_PREFIX && sequence[1] == 91 && sequence[2] == 68;
    }

    @Override
    public boolean isKeyRightSequence(int... sequence) {
        return sequence != null && sequence.length >= 3 && sequence[0] == ARROW_PREFIX && sequence[1] == 91 && sequence[2] == 67;
    }

}
