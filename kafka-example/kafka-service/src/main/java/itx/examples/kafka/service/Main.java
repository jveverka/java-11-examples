package itx.examples.kafka.service;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import static itx.examples.kafka.dto.KafkaConstants.KAFKA_BROKERS;
import static itx.examples.kafka.dto.KafkaConstants.TOPIC_SERVICE_REQUESTS;


public class Main {

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        Properties settings = new Properties();
        settings.put("bootstrap.servers", KAFKA_BROKERS);
        settings.put("client.id", "client-id");
        settings.put("group.id", "consumer-group");
        settings.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        settings.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        Collection<String> topics = Collections.singletonList(TOPIC_SERVICE_REQUESTS);

        try(Consumer<String, String> consumer = new KafkaConsumer<>(settings)) {
            consumer.subscribe(topics);

            while (true) {
                ConsumerRecords<String, String> record = consumer.poll(Duration.ofMillis(10));
                if (!record.isEmpty()) {
                    record.iterator().forEachRemaining(r ->
                        LOG.info("{}:{}", r.key(), r.value())
                    );
                    break;
                }
            }
        }

    }

}
