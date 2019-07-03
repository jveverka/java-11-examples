package itx.examples.kafka.client;

import itx.examples.kafka.ProcessingException;
import itx.examples.kafka.dto.ServiceRequest;
import itx.examples.kafka.dto.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ClientApp {

    private static final Logger LOG = LoggerFactory.getLogger(ClientApp.class);

    public static void main(String[] args) throws ProcessingException, ExecutionException, InterruptedException {
        try (ProcessingServiceClient processingService = new ProcessingServiceClient()) {
            processingService.init();
            for (int i = 0; i < 10; i++) {
                ServiceRequest serviceRequest = new ServiceRequest("t1", "hi[" + i + "]");
                LOG.info("Request: {}:{}", serviceRequest.getTaskId(), serviceRequest.getData());
                Future<ServiceResponse> process = processingService.process(serviceRequest);
                ServiceResponse serviceResponse = process.get();
                LOG.info("Response[{}]: {}:{}:{}", i, serviceResponse.getTaskId(), serviceResponse.getData(), serviceResponse.getResponse());
            }
        }
    }

}
