package itx.examples.springbank.server.controller;

import itx.examples.springbank.common.dto.CreateClientRequest;
import itx.examples.springbank.common.dto.ClientId;
import itx.examples.springbank.common.dto.ServiceException;
import itx.examples.springbank.server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/services/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("client")
    public ClientId createClient(CreateClientRequest createClientRequest) throws ServiceException {
        return adminService.createClient(createClientRequest);
    }

}
