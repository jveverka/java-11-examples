package itx.examples.kafka.service;

import com.beust.jcommander.Parameter;

import static itx.examples.kafka.KafkaConstants.KAFKA_BROKERS;

public class Arguments {

    @Parameter(names = {"-i", "--identity" }, description = "Unique service Id")
    private String serviceId = "default-id";

    @Parameter(names = {"-b", "--brokers" }, description = "Comma separated list of broker addresses.")
    private String brokers = KAFKA_BROKERS;

    public String getServiceId() {
        return serviceId;
    }

    public String getBrokers() {
        return brokers;
    }

}
