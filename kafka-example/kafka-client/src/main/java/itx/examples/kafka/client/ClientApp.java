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
        try (ProcessingServiceClient processingService = new ProcessingServiceClient()) {
            processingService.init();
            String taskId = UUID.randomUUID().toString();
            for (int i = 0; i < 10; i++) {
                long timeStamp = System.nanoTime();
                ServiceRequest serviceRequest = new ServiceRequest(taskId, arguments.getClientId(),"hi[" + i + "]");
                LOG.info("Request: {}:{}:{}", arguments.getClientId(), serviceRequest.getTaskId(), serviceRequest.getData());
                Future<ServiceResponse> process = processingService.process(serviceRequest);
                ServiceResponse serviceResponse = process.get();
                float delay = (System.nanoTime() - timeStamp) / 1_000_000F;
                LOG.info("Response[{}]: {}:{}:{} {}ms", i, serviceResponse.getTaskId(), serviceResponse.getData(), serviceResponse.getResponse(), delay);
                Thread.sleep(6000);
            }
            LOG.info("kafka client done.");
        }
    }

}
