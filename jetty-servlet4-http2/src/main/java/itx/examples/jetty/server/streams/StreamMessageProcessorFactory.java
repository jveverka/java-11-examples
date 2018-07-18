package itx.examples.jetty.server.streams;

import itx.examples.jetty.common.services.MessageServiceAsync;
import itx.examples.jetty.server.http2.StreamProcessorFactory;
import org.eclipse.jetty.http2.api.Stream;

public class StreamMessageProcessorFactory implements StreamProcessorFactory {

    final private MessageServiceAsync messageService;

    public StreamMessageProcessorFactory(MessageServiceAsync messageService) {
        this.messageService = messageService;
    }

    @Override
    public Stream.Listener create(Stream stream) {
        return new StreamMessageProcessor(stream, messageService);
    }

}
