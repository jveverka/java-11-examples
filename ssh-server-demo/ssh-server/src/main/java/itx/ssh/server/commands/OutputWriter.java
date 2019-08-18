package itx.ssh.server.commands;

import java.io.IOException;

/**
 * Proxy for writing data into stdout and stderr output streams.
 */
public interface OutputWriter {

    /**
     * Write byte into stdout and flush.
     * @param b data to write.
     * @throws IOException
     */
    void writeStdOutAndFlush(byte b) throws IOException;

    /**
     * Write bytes into stdout and flush.
     * @param bytes data to write.
     * @throws IOException
     */
    void writeStdOutAndFlush(byte[] bytes) throws IOException;

    /**
     * Write byte into stdout and flush.
     * @param b data to write.
     * @throws IOException
     */
    void writeStdErrAndFlush(byte b) throws IOException;

    /**
     * Write bytes into stdout and flush.
     * @param bytes data to write.
     * @throws IOException
     */
    void writeStdErrAndFlush(byte[] bytes) throws IOException;

    /**
     * Write message bytes into stdout appends character 13 (ENTER) and flush.
     * @param bytes message payload to write.
     * @throws IOException
     */
    void writeMessage(byte[] bytes) throws IOException;

}
