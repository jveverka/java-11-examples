package itx.examples.springboot.demo.dto;

public class SystemInfo {

    private final String name;
    private final String version;
    private final long timestamp;

    public SystemInfo(String name, String version, long timestamp) {
        this.name = name;
        this.version = version;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public long getTimestamp() {
        return timestamp;
    }
    
}
