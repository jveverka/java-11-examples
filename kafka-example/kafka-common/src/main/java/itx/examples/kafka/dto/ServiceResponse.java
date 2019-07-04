package itx.examples.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceResponse {

    private final String taskId;
    private final String clientId;
    private final String data;
    private final String response;

    @JsonCreator
    public ServiceResponse(@JsonProperty("taskId") String taskId,
                           @JsonProperty("clientId") String clientId,
                           @JsonProperty("data") String data,
                           @JsonProperty("response") String response) {
        this.taskId = taskId;
        this.clientId = clientId;
        this.data = data;
        this.response = response;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getData() {
        return data;
    }

    public String getResponse() {
        return response;
    }

}
