package itx.examples.kafka.client;

import com.beust.jcommander.JCommander;
import itx.examples.kafka.ProcessingException;
import itx.examples.kafka.dto.ServiceRequest;
import itx.examples.kafka.dto.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ClientApp {

    private static final Logger LOG = LoggerFactory.getLogger(ClientApp.class);

    public static void main(String[] args) throws ProcessingException, ExecutionException, InterruptedException {
        Arguments arguments = new Arguments();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);

        LOG.info("Kafka client started {} ...", arguments.getClientId());
        try (ProcessingServiceClient processingService = new ProcessingServiceClient(arguments.getClientId(), arguments.getBrokers())) {
            processingService.init();
            String taskId = UUID.randomUUID().toString();
            float delays = 0;
            for (int i = 0; i < arguments.getMessageCount(); i++) {
                long timeStamp = System.nanoTime();
                ServiceRequest serviceRequest = new ServiceRequest(taskId, arguments.getClientId(),"hi[" + i + "]");
                LOG.info("Request: {}:{}:{}", arguments.getClientId(), serviceRequest.getTaskId(), serviceRequest.getData());
                Future<ServiceResponse> process = processingService.process(serviceRequest);
                ServiceResponse serviceResponse = process.get();
                float delay = (System.nanoTime() - timeStamp) / 1_000_000F;
                String eval = checkResponse(serviceRequest, serviceResponse);
                LOG.info("Response[{}]: {}:{}:{} {} {}ms", i, serviceResponse.getTaskId(), serviceResponse.getData(), serviceResponse.getResponse(), eval, delay);
                delays = delays + delay;
                if (arguments.getMessageDelay() > 0) {
                    Thread.sleep(arguments.getMessageDelay());
                }
            }
            LOG.info("Client {} send messages {} with avg. latency: {}ms", arguments.getClientId(), arguments.getMessageCount(), (delays/arguments.getMessageCount()));
            LOG.info("Kafka client done.");
        }
    }

    public static String checkResponse(ServiceRequest request, ServiceResponse response) {
        if (request.getTaskId().equals(response.getTaskId())
                && request.getClientId().equals(response.getClientId())
                && request.getData().equals(response.getData())) {
            return "OK";
        }
        return "ERROR";
    }

}
