package itx.examples.kafka.service;

import com.beust.jcommander.JCommander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceApp {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceApp.class);

    public static void main(String[] args) {
        Arguments arguments = new Arguments();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);

        LOG.info("Started kafka service {}", arguments.getServiceId());
        ProcessingServiceBackend processingServiceBackend = new ProcessingServiceBackend(arguments.getServiceId(), arguments.getBrokers());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                processingServiceBackend.shutdown();
            }
        });
        processingServiceBackend.start();
    }

}
