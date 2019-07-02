package itx.examples.kafka.client;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static itx.examples.kafka.dto.KafkaConstants.KAFKA_BROKERS;
import static itx.examples.kafka.dto.KafkaConstants.TOPIC_SERVICE_REQUESTS;

public class Main {

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        Properties settings = new Properties();
        settings.put("bootstrap.servers", KAFKA_BROKERS);
        settings.put("client.id", "client-id");
        settings.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        settings.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try(KafkaProducer<String, String> producer = new KafkaProducer<>(settings)) {
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_SERVICE_REQUESTS, "key","hello");
            producer.send(record);
        }
        LOG.info("done.");

    }

}
