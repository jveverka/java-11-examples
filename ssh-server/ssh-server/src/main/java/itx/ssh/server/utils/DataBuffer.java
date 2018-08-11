package itx.ssh.server.utils;

import java.util.Arrays;

public class DataBuffer {

    private static final byte EMPTY = 0;

    private byte[] dataBuffer;
    private int cursorWaterMark;

    public DataBuffer() {
        this.dataBuffer = new byte[128];
        this.cursorWaterMark = EMPTY;
    }

    public void add(byte b) {
        if (cursorWaterMark >= dataBuffer.length) {
            byte[] newDataBuffer = new byte[dataBuffer.length + 128];
            for (int i=0; i<dataBuffer.length; i++) {
                newDataBuffer[i] = dataBuffer[i];
            }
            this.dataBuffer = newDataBuffer;
        }
        dataBuffer[cursorWaterMark] = b;
        cursorWaterMark++;
    }

    public void add(byte[] bytes) {
        for (int i=0; i<bytes.length; i++) {
            add(bytes[i]);
        }
    }

    public void resetAndAdd(byte[] bytes) {
        reset();
        add(bytes);
    }

    public void reset() {
        for (int i=0; i<dataBuffer.length; i++) {
            dataBuffer[i] = EMPTY;
        }
        cursorWaterMark = EMPTY;
    }

    public byte[] getAndReset() {
        byte[] result = get();
        reset();
        return result;
    }

    public byte get(int index) {
        return dataBuffer[index];
    }

    public byte[] get() {
        return Arrays.copyOf(dataBuffer, cursorWaterMark);
    }

    public int getCursorWaterMark() {
        return cursorWaterMark;
    }

    public int getMaxSize() {
        return dataBuffer.length;
    }

}
