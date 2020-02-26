package itx.examples.springboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemInfo {

    private final String name;
    private final String version;
    private final long timestamp;

    @JsonCreator
    public SystemInfo(@JsonProperty("name") String name,
                      @JsonProperty("version") String version,
                      @JsonProperty("timestamp") long timestamp) {
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
