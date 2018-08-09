package itx.examples.sshd.commands.repl;

/**
 * renders single commandline content
 * supports editing (left, right arrow, backspace)
 */
public class CommandRenderer {

    private final static int BUF_MAX = 2048;
    private final static char EMPTY = 0;

    private int cursorPosition;
    private char[] cmdBuffer;

    public CommandRenderer() {
        this.cmdBuffer = new char[BUF_MAX];
    }

    public void onKeyLeft() {
        if (cursorPosition > 0) {
            cursorPosition--;
        }
    }

    public void onKeyRight() {
        if (cursorPosition < (getCommandLength()-1)) {
            cursorPosition++;
        }
    }

    public void onBackSpace() {
        if (cursorPosition > 0) {
            cursorPosition--;
            shiftLeft();
        }
    }

    public void onDeleteKey() {
        shiftLeft();
    }

    public void onCharInsert(char ch) {
        shiftRight();
        this.cmdBuffer[cursorPosition] = ch;
        if (cursorPosition < cmdBuffer.length) {
            cursorPosition++;
        }
    }

    public String getCommandAndReset() {
        String command = getCommand();
        reset();
        return command;
    }

    public String getCommand() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < cmdBuffer.length; i++) {
            if (cmdBuffer[i] == EMPTY) {
                break;
            }
            sb.append(cmdBuffer[i]);
        }
        return sb.toString();
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    public boolean isCursorInEndLinePosition() {
        return cmdBuffer[cursorPosition] == EMPTY;
    }

    public void reset() {
        cursorPosition = 0;
        for (int i=0; i < cmdBuffer.length; i++) {
            cmdBuffer[i] = EMPTY;
        }
    }

    private void shiftRight() {
        for (int i = (cmdBuffer.length-2); i>=cursorPosition; i--) {
            cmdBuffer[i+1] = cmdBuffer[i];
        }
    }

    private void shiftLeft() {
        for (int i = cursorPosition; i<(cmdBuffer.length-1); i++) {
            cmdBuffer[i] = cmdBuffer[i+1];
        }
    }

    private int getCommandLength() {
        for (int i=0; i < cmdBuffer.length; i++) {
            if (cmdBuffer[i] == EMPTY) {
                return i+1;
            }
        }
        return BUF_MAX;
    }

}
