package itx.ssh.server.utils;

import itx.ssh.server.commands.OutputWriter;

import java.io.IOException;
import java.io.OutputStream;

public class OutputWriterImpl implements OutputWriter {

    private static final int ENTER = 13;

    private final OutputStream stdout;
    private final OutputStream stderr;

    public OutputWriterImpl(OutputStream stdout, OutputStream stderr) {
        this.stdout = stdout;
        this.stderr = stderr;
    }

    @Override
    public void writeStdOutAndFlush(byte b) throws IOException {
        synchronized (stdout) {
            stdout.write(b);
            stdout.flush();
        }
    }

    @Override
    public void writeStdOutAndFlush(byte[] bytes) throws IOException {
        synchronized (stdout) {
            stdout.write(bytes);
            stdout.flush();
        }
    }

    @Override
    public void writeStdErrAndFlush(byte b) throws IOException {
        synchronized(stderr) {
            stderr.write(b);
            stderr.flush();
        }
    }

    @Override
    public void writeStdErrAndFlush(byte[] bytes) throws IOException {
        synchronized(stderr) {
            stderr.write(bytes);
            stderr.flush();
        }
    }

    public void writeStdErr(byte b) throws IOException {
        synchronized(stderr) {
            stderr.write(b);
        }
    }

    public void writeStdErr(byte[] bytes) throws IOException {
        synchronized(stderr) {
            stderr.write(bytes);
        }
    }

    public void flushStdErr() throws IOException {
        synchronized(stderr) {
            stderr.flush();
        }
    }

    public void writeStdOut(byte b) throws IOException {
        synchronized(stdout) {
            stdout.write(b);
        }
    }

    public void writeStdOut(byte[] bytes) throws IOException {
        synchronized(stdout) {
            stdout.write(bytes);
        }
    }

    public void flushStdOut() throws IOException {
        synchronized(stdout) {
            stdout.flush();
        }
    }

    @Override
    public void writeMessage(byte[] bytes) throws IOException {
        synchronized (stdout) {
            stdout.write(bytes);
            stdout.write(ENTER);
            stdout.flush();
        }
    }

}
