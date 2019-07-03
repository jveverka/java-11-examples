package itx.examples.kafka.service;

import com.beust.jcommander.Parameter;

public class Arguments {

    @Parameter(names = {"-i", "--identity" }, description = "Unique service Id")
    private String serviceId = "default-id";

    public String getServiceId() {
        return serviceId;
    }

}
