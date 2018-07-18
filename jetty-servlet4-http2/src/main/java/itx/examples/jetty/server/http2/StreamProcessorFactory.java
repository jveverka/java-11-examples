package itx.examples.jetty.server.http2;

import org.eclipse.jetty.http2.api.Stream;

/**
 * This factory is used to create new instance of
 * stream listener for each new htt2 stream.
 */
public interface StreamProcessorFactory {

    /**
     * creates an instance of stream listener
     * @param stream
     *   initial stream
     * @return
     *   new instance of stream listener
     */
    Stream.Listener create(Stream stream);

}
