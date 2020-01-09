package itx.examples.rmq.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class ClientMain {

    private static final Logger LOG = LoggerFactory.getLogger(ClientMain.class);

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
        LOG.info("Client Service starting ...");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@localhost:15672/virtualHost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("exchangeName", "direct", true);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, "exchangeName", "publishKey");

        channel.close();
        connection.close();
    }

}
