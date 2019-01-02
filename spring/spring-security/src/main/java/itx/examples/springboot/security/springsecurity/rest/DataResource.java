package itx.examples.springboot.security.springsecurity.rest;

import itx.examples.springboot.security.springsecurity.services.DataService;
import itx.examples.springboot.security.springsecurity.services.dto.ServerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services/data/")
public class DataResource {

    @Autowired
    private DataService dataService;

    @GetMapping("/all")
    public ResponseEntity<ServerData> getData() {
        return ResponseEntity.ok().body(dataService.getSecuredData("Secured"));
    }

}
