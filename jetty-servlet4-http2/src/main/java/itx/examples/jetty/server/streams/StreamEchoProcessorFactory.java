package itx.examples.jetty.server.streams;

import itx.examples.jetty.common.services.EchoService;
import itx.examples.jetty.server.http2.StreamProcessorFactory;
import org.eclipse.jetty.http2.api.Stream;

public class StreamEchoProcessorFactory implements StreamProcessorFactory {

    private EchoService echoService;

    public StreamEchoProcessorFactory(EchoService echoService) {
        this.echoService = echoService;
    }

    @Override
    public Stream.Listener create(Stream stream) {
        return new StreamEchoProcessor(echoService);
    }

}
