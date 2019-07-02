package itx.examples.kafka.service;

import itx.examples.kafka.dto.ServiceRequest;
import itx.examples.kafka.dto.ServiceResponse;

public interface ProcessingService {

    ServiceResponse process(ServiceRequest request);

}
