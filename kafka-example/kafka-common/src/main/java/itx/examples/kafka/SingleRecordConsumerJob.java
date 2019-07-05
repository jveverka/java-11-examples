package itx.examples.kafka;

import itx.examples.kafka.dto.ServiceResponse;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.utils.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.Callable;


public class SingleRecordConsumerJob implements Callable<ServiceResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(SingleRecordConsumerJob.class);

    private final Consumer<String, Bytes> consumer;
    private final DataMapper dataMapper;
    private final String key;

    public SingleRecordConsumerJob(Consumer<String, Bytes> consumer, String key) {
        LOG.info("Key: {}", key);
        this.consumer = consumer;
        this.dataMapper = new DataMapper();
        this.key = key;
    }

    @Override
    public ServiceResponse call() throws Exception {
        LOG.info("Consumer thread started.");
        while (true) {
            ConsumerRecords<String, Bytes> records = consumer.poll(Duration.ofMillis(10));
            if (!records.isEmpty()) {
                for (ConsumerRecord<String, Bytes> record: records) {
                    if (key.equals(record.key())) {
                        LOG.info("Record: {}", record.key());
                        LOG.info("received response");
                        return dataMapper.deserialize(record.value(), ServiceResponse.class);
                    }
                }
            }
        }
    }

}
