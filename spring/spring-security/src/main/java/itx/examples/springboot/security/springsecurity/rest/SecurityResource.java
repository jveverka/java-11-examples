package itx.examples.springboot.security.springsecurity.rest;

import itx.examples.springboot.security.springsecurity.services.UserAccessService;
import itx.examples.springboot.security.springsecurity.services.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/services/security/")
public class SecurityResource {

    @Autowired
    private UserAccessService userAccessService;

    @Autowired
    private HttpSession httpSession;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        if (userAccessService.login(httpSession.getId(), loginRequest)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/logout")
    public ResponseEntity logout() {
        userAccessService.logout(httpSession.getId());
        return ResponseEntity.ok().build();
    }
}