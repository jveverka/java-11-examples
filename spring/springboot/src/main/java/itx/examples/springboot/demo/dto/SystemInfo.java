package itx.examples.springboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemInfo {

    private final String id;
    private final String name;
    private final String version;
    private final long timestamp;

    @JsonCreator
    public SystemInfo(@JsonProperty("id") String id,
                      @JsonProperty("name") String name,
                      @JsonProperty("version") String version,
                      @JsonProperty("timestamp") long timestamp) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
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
