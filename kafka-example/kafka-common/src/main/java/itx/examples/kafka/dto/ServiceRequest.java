package itx.examples.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequest {

    private final String taskId;
    private final String clientId;
    private final String data;

    @JsonCreator
    public ServiceRequest(@JsonProperty("taskId") String taskId,
                          @JsonProperty("clientId") String clientId,
                          @JsonProperty("data") String data) {
        this.taskId = taskId;
        this.clientId = clientId;
        this.data = data;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getData() {
        return data;
    }

    public String getClientId() {
        return clientId;
    }

}
