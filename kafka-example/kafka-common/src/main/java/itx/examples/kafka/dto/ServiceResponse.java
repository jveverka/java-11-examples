package itx.examples.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceResponse {

    private final String taskId;
    private final String data;
    private final String response;

    @JsonCreator
    public ServiceResponse(@JsonProperty("taskId") String taskId,
                           @JsonProperty("data") String data,
                           @JsonProperty("response") String response) {
        this.taskId = taskId;
        this.data = data;
        this.response = response;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getData() {
        return data;
    }

    public String getResponse() {
        return response;
    }

}
