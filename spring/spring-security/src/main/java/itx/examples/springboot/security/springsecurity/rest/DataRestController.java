package itx.examples.springboot.security.springsecurity.rest;

import itx.examples.springboot.security.springsecurity.services.DataService;
import itx.examples.springboot.security.springsecurity.services.dto.ServerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/services/data/")
public class DataRestController {

    private static final Logger LOG = LoggerFactory.getLogger(DataRestController.class);

    @Autowired
    private DataService dataService;

    @GetMapping("/all")
    public ResponseEntity<ServerData> getData() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        LOG.info("getData: authentication={}", securityContext.getAuthentication().getName());
        securityContext.getAuthentication().getAuthorities().forEach(a->{
            LOG.info("authority={}", a.getAuthority());
        });
        return ResponseEntity.ok().body(dataService.getSecuredData("Secured for " + securityContext.getAuthentication().getName()));
    }

}
