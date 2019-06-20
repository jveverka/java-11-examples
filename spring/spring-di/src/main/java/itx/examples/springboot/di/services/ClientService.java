package itx.examples.springboot.di.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ClientService {

    @Autowired
    private PrintService printService;

    @Autowired
    @Qualifier("stdErr")
    private PrintService printServiceErr;

    @Autowired
    @Qualifier("stdOut")
    private PrintService printServiceOut;

    @Autowired
    private DataService dataService;

    @PostConstruct
    public void init() {
        printService.print("init ...\n");
        printServiceErr.print("init ...\n");
        printService.print(dataService.getData() + "\n");
        printServiceOut.print("init ...\n");
    }

    public void print(String message) {
        printService.print(message);
    }

}
