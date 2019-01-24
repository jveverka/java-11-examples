package itx.examples.springboot.angular.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services/app/")
public class ApplicationInfo {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationInfo.class);

    @GetMapping("/info")
    public ResponseEntity<AppInfo> getAppInfo() {
        LOG.info("getAppInfo:");
        AppInfo appInfo = new AppInfo("spring-angular", "1.0.0", System.currentTimeMillis());
        return ResponseEntity.ok().body(appInfo);
    }

}
