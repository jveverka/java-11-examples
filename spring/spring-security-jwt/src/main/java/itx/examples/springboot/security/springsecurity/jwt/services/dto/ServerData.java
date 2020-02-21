package itx.examples.springboot.security.springsecurity.jwt.services.dto;

public class ServerData {

    private final String source;
    private final String data;
    private final long timestamp;

    public ServerData(String source, String data, long timestamp) {
        this.source = source;
        this.data = data;
        this.timestamp = timestamp;
    }

    public String getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }
}
