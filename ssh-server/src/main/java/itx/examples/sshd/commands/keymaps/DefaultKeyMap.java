package itx.examples.sshd.commands.keymaps;

import java.util.Arrays;

public class DefaultKeyMap implements KeyMap {

    final private static int ENTER = 13;
    final private static int BACKSPACE = 127;
    final private static int SEQUENCE_PREFIX = 27;
    final private static int ARROW_PREFIX = 91;
    final private static int[] ARROW_LEFT_SEQUENCE = { SEQUENCE_PREFIX, ARROW_PREFIX, 68 };
    final private static int[] ARROW_RIGHT_SEQUENCE = { SEQUENCE_PREFIX, ARROW_PREFIX, 67 };
    final private static int[] DELETE_SEQUENCE = {SEQUENCE_PREFIX, ARROW_PREFIX, 51 };

    @Override
    public int getEnterKeyCode() {
        return ENTER;
    }

    @Override
    public int getBackSpaceKeyCode() {
        return BACKSPACE;
    }

    @Override
    public int getSequencePrefix() {
        return SEQUENCE_PREFIX;
    }

    @Override
    public boolean isKeyLeftSequence(int... sequence) {
        return sequence != null && sequence.length >= 3 &&
                sequence[0] == ARROW_LEFT_SEQUENCE[0] &&
                sequence[1] == ARROW_LEFT_SEQUENCE[1] &&
                sequence[2] == ARROW_LEFT_SEQUENCE[2];
    }

    @Override
    public boolean isKeyRightSequence(int... sequence) {
        return sequence != null && sequence.length >= 3 &&
                sequence[0] == ARROW_RIGHT_SEQUENCE[0] &&
                sequence[1] == ARROW_RIGHT_SEQUENCE[1] &&
                sequence[2] == ARROW_RIGHT_SEQUENCE[2];
    }

    @Override
    public boolean isKeyDeleteSequence(int... sequence) {
        return sequence != null && sequence.length >= 3 &&
                sequence[0] == DELETE_SEQUENCE[0] &&
                sequence[1] == DELETE_SEQUENCE[1] &&
                sequence[2] == DELETE_SEQUENCE[2];
    }

    @Override
    public int[] getKeyLeftSequence() {
        return Arrays.copyOf(ARROW_LEFT_SEQUENCE, ARROW_LEFT_SEQUENCE.length);
    }

    @Override
    public int[] getKeyRightSequence() {
        return Arrays.copyOf(ARROW_RIGHT_SEQUENCE, ARROW_RIGHT_SEQUENCE.length);
    }

    @Override
    public int[] getDeleteSequence() {
        return Arrays.copyOf(DELETE_SEQUENCE, DELETE_SEQUENCE.length);
    }

}
