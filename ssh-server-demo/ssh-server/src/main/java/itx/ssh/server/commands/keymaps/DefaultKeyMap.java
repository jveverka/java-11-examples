package itx.ssh.server.commands.keymaps;

import java.util.Arrays;

public class DefaultKeyMap implements KeyMap {

    final private static byte ENTER = 13;
    final private static byte BACKSPACE = 127;
    final private static byte SEQUENCE_PREFIX = 27;
    final private static byte ARROW_PREFIX = 91;
    final private static byte[] ARROW_LEFT_SEQUENCE = { SEQUENCE_PREFIX, ARROW_PREFIX, 68 };
    final private static byte[] ARROW_RIGHT_SEQUENCE = { SEQUENCE_PREFIX, ARROW_PREFIX, 67 };
    final private static byte[] DELETE_SEQUENCE = {SEQUENCE_PREFIX, ARROW_PREFIX, 51 };
    final private static byte[] HOME_SEQUENCE = {SEQUENCE_PREFIX, ARROW_PREFIX, 72 };
    final private static byte[] END_SEQUENCE = {SEQUENCE_PREFIX, ARROW_PREFIX, 70 };

    @Override
    public byte getEnterKeyCode() {
        return ENTER;
    }

    @Override
    public byte getBackSpaceKeyCode() {
        return BACKSPACE;
    }

    @Override
    public byte getSequencePrefix() {
        return SEQUENCE_PREFIX;
    }

    @Override
    public boolean isKeyLeftSequence(byte... sequence) {
        return sequence != null && sequence.length >= 3 &&
                sequence[0] == ARROW_LEFT_SEQUENCE[0] &&
                sequence[1] == ARROW_LEFT_SEQUENCE[1] &&
                sequence[2] == ARROW_LEFT_SEQUENCE[2];
    }

    @Override
    public boolean isKeyRightSequence(byte... sequence) {
        return sequence != null && sequence.length >= 3 &&
                sequence[0] == ARROW_RIGHT_SEQUENCE[0] &&
                sequence[1] == ARROW_RIGHT_SEQUENCE[1] &&
                sequence[2] == ARROW_RIGHT_SEQUENCE[2];
    }

    @Override
    public boolean isKeyDeleteSequence(byte... sequence) {
        return sequence != null && sequence.length >= 3 &&
                sequence[0] == DELETE_SEQUENCE[0] &&
                sequence[1] == DELETE_SEQUENCE[1] &&
                sequence[2] == DELETE_SEQUENCE[2];
    }

    @Override
    public boolean isKeyHomeSequence(byte... sequence) {
        return sequence != null && sequence.length >= 3 &&
                sequence[0] == HOME_SEQUENCE[0] &&
                sequence[1] == HOME_SEQUENCE[1] &&
                sequence[2] == HOME_SEQUENCE[2];
    }

    @Override
    public boolean isKeyEndSequence(byte... sequence) {
        return sequence != null && sequence.length >= 3 &&
                sequence[0] == END_SEQUENCE[0] &&
                sequence[1] == END_SEQUENCE[1] &&
                sequence[2] == END_SEQUENCE[2];
    }

    @Override
    public byte[] getKeyLeftSequence() {
        return Arrays.copyOf(ARROW_LEFT_SEQUENCE, ARROW_LEFT_SEQUENCE.length);
    }

    @Override
    public byte[] getKeyRightSequence() {
        return Arrays.copyOf(ARROW_RIGHT_SEQUENCE, ARROW_RIGHT_SEQUENCE.length);
    }

    @Override
    public byte[] getKeyDeleteSequence() {
        return Arrays.copyOf(DELETE_SEQUENCE, DELETE_SEQUENCE.length);
    }

    @Override
    public byte[] getKeyHomeSequence() {
        return Arrays.copyOf(HOME_SEQUENCE, HOME_SEQUENCE.length);
    }

    @Override
    public byte[] getKeyEndSequence() {
        return Arrays.copyOf(END_SEQUENCE, END_SEQUENCE.length);
    }

}
