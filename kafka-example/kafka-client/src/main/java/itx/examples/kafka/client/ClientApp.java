package itx.examples.kafka.client;

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
        LOG.info("kafka client started ...");
        try (ProcessingServiceClient processingService = new ProcessingServiceClient()) {
            processingService.init();
            String taskId = UUID.randomUUID().toString();
            for (int i = 0; i < 10; i++) {
                ServiceRequest serviceRequest = new ServiceRequest(taskId, "hi[" + i + "]");
                LOG.info("Request: {}:{}", serviceRequest.getTaskId(), serviceRequest.getData());
                Future<ServiceResponse> process = processingService.process(serviceRequest);
                ServiceResponse serviceResponse = process.get();
                LOG.info("Response[{}]: {}:{}:{}", i, serviceResponse.getTaskId(), serviceResponse.getData(), serviceResponse.getResponse());
                Thread.sleep(5000);
            }
            LOG.info("kafka client done.");
        }
    }

}
