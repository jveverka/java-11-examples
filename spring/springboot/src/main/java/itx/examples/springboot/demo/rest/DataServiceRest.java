package itx.examples.springboot.demo.rest;

import itx.examples.springboot.demo.dto.DataMessage;
import itx.examples.springboot.demo.dto.SystemInfo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/data")
public class DataServiceRest {

    @GetMapping(path = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public SystemInfo getSystemInfo() {
        return new SystemInfo("spring-demo", "1.0.0", System.currentTimeMillis());
    }

    @PostMapping(path = "/message", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody DataMessage getDataMessage(@RequestBody DataMessage dataMessage) {
        return new DataMessage(dataMessage.getData());
    }

}
