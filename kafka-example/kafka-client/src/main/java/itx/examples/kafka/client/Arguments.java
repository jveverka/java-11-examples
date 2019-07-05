package itx.examples.kafka.client;

import com.beust.jcommander.Parameter;

import static itx.examples.kafka.KafkaConstants.KAFKA_BROKERS;

public class Arguments {

    @Parameter(names = {"-i", "--identity" }, description = "Unique client Id.")
    private String clientId = "default-id";

    @Parameter(names = {"-d", "--delay" }, description = "Delay between messages while sending.")
    private Long messageDelay = 3000L;

    @Parameter(names = {"-c", "--count" }, description = "Number od messages to be send.")
    private Integer messageCount = 10;

    @Parameter(names = {"-b", "--brokers" }, description = "Comma separated list of broker addresses.")
    private String brokers = KAFKA_BROKERS;

    public String getClientId() {
        return clientId;
    }

    public Long getMessageDelay() {
        return messageDelay;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public String getBrokers() {
        return brokers;
    }
}
