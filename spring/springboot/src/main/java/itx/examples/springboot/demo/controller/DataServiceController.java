package itx.examples.springboot.demo.controller;

import itx.examples.springboot.demo.dto.DataMessage;
import itx.examples.springboot.demo.dto.RequestInfo;
import itx.examples.springboot.demo.dto.generic.ComplexDataPayload;
import itx.examples.springboot.demo.dto.generic.DataMarker;
import itx.examples.springboot.demo.dto.generic.GenericRequest;
import itx.examples.springboot.demo.dto.generic.GenericResponse;
import itx.examples.springboot.demo.dto.SystemInfo;
import itx.examples.springboot.demo.dto.generic.SimpleDataPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/data")
public class DataServiceController {

    private static final Logger LOG = LoggerFactory.getLogger(DataServiceController.class);

    @GetMapping(path = "/info", produces = MediaType.APPLICATION_JSON_VALUE )
    public SystemInfo getSystemInfo() {
        return new SystemInfo("spring-demo", "1.0.0", System.currentTimeMillis());
    }

    @PostMapping(path = "/message", consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<DataMessage> getDataMessage(@RequestBody DataMessage dataMessage) {
        DataMessage responseMessage = new DataMessage(dataMessage.getData());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(responseMessage, responseHeaders, HttpStatus.OK);
    }

    @GetMapping(path = "/echo/{message}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<DataMessage> getEcho(@PathVariable String message) {
        DataMessage responseMessage = new DataMessage(message);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping(path = "/generics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse<? extends DataMarker> getGenericResponse(@RequestBody GenericRequest<? extends DataMarker> request) {
        LOG.info("getGenericResponse");
        if (request.getData() instanceof SimpleDataPayload) {
            SimpleDataPayload payload = (SimpleDataPayload) request.getData();
            return new GenericResponse<>(request.getName(), payload);
        } else if (request.getData() instanceof ComplexDataPayload) {
            ComplexDataPayload payload = (ComplexDataPayload) request.getData();
            return new GenericResponse<>(request.getName(), payload);
        } else {
            throw new UnsupportedOperationException("Unsupported type");
        }
    }

    @GetMapping(path = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestInfo> getRequestParameters(HttpServletRequest request) {
        LOG.info("getRequestParameters: {}?{}", request.getRequestURL(), request.getQueryString());
        RequestInfo requestInfo = new RequestInfo(request.getRequestURL().toString(), request.getQueryString());
        return ResponseEntity.ok(requestInfo);
    }

}
