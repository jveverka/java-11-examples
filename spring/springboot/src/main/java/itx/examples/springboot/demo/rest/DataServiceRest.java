package itx.examples.springboot.demo.rest;

import itx.examples.springboot.demo.dto.DataMessage;
import itx.examples.springboot.demo.dto.SystemInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/data")
public class DataServiceRest {

    @GetMapping(path = "/info", produces = MediaType.APPLICATION_JSON_VALUE )
    public SystemInfo getSystemInfo() {
        return new SystemInfo("spring-demo", "1.0.0", System.currentTimeMillis());
    }

    @PostMapping(path = "/message", consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<DataMessage> getDataMessage(@RequestBody DataMessage dataMessage) {
        DataMessage responseMessage = new DataMessage(dataMessage.getData());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<DataMessage>(responseMessage, responseHeaders, HttpStatus.OK);
    }

}
