package itx.examples.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequest {

    private final String taskId;
    private final String data;

    @JsonCreator
    public ServiceRequest(@JsonProperty("taskId") String taskId,
                          @JsonProperty("data") String data) {
        this.taskId = taskId;
        this.data = data;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getData() {
        return data;
    }

}
