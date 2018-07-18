package itx.examples.jetty.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemInfo {

    private Long systemTime;
    private String applicationName;
    private String applicationVersion;

    @JsonCreator
    public SystemInfo(@JsonProperty("systemTime") Long systemTime,
                      @JsonProperty("applicationName") String applicationName,
                      @JsonProperty("applicationVersion") String applicationVersion) {
        this.systemTime = systemTime;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
    }

    public Long getSystemTime() {
        return systemTime;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

}
