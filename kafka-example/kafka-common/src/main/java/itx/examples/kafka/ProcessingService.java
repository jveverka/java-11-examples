package itx.examples.kafka;

import itx.examples.kafka.dto.ServiceRequest;
import itx.examples.kafka.dto.ServiceResponse;

import java.util.concurrent.Future;

public interface ProcessingService {

    Future<ServiceResponse> process(ServiceRequest request) throws ProcessingException;

}
