package itx.ssh.server.commands.repl;

import java.nio.charset.Charset;

/**
 * renders single commandline content
 * supports editing (left, right arrow, backspace)
 * internal character buffer is 0 terminated
 */
public class CommandRenderer {

    private final static int BUF_MAX = 2048;
    private final static char EMPTY = 0;

    private int cursorPosition;
    private byte[] cmdBuffer;

    public CommandRenderer() {
        this.cmdBuffer = new byte[BUF_MAX];
    }

    public void onKeyHome() {
        cursorPosition = 0;
    }

    public void onKeyEnd() {
        int cmdLength = getCommandLength();
        if (cmdLength == 0) {
            cursorPosition = 0;
        } else {
            cursorPosition = cmdLength;
        }
    }

    public void onKeyLeft() {
        if (cursorPosition > 0) {
            cursorPosition--;
        }
    }

    public void onKeyRight() {
        if (cursorPosition < getCommandLength()) {
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

    public void onCharInsert(byte ch) {
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

    public byte[] getCommandBytesAndReset() {
        byte[] command = getCommandBytes();
        reset();
        return command;
    }

    public String getCommand() {
        return new String(getCommandBytes(), Charset.forName("UTF-8"));
    }

    public byte[] getCommandBytes() {
        byte[] data = new byte[getCommandLength()];
        for (int i=0; i < data.length; i++) {
            data[i] = cmdBuffer[i];
        }
        return data;
    }

    public int getCommandLength() {
        if (cmdBuffer[0] == EMPTY) {
            return 0;
        }
        for (int i=0; i < cmdBuffer.length; i++) {
            if (cmdBuffer[i] == EMPTY) {
                return i;
            }
        }
        return BUF_MAX;
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

}
