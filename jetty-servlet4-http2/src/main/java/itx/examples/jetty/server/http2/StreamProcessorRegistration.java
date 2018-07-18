package itx.examples.jetty.server.http2;

public class StreamProcessorRegistration {

    private String urn;
    private StreamProcessorFactory factory;

    public StreamProcessorRegistration(String urn, StreamProcessorFactory factory) {
        this.factory = factory;
        this.urn = urn;
    }

    public String getUrn() {
        return urn;
    }

    public StreamProcessorFactory getFactory() {
        return factory;
    }

}
