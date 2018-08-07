package itx.examples.sshd.commands.keymaps;

public class DefaultKeyMap implements KeyMap {

    final private static int ENTER = 13;
    final private static int BACKSPACE = 127;

    @Override
    public int getEnterKeyCode() {
        return ENTER;
    }

    @Override
    public int getBackSpaceKeyCode() {
        return BACKSPACE;
    }

}
