package itx.examples.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceApp {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceApp.class);

    public static void main(String[] args) {
        LOG.info("started kafka service");
        ProcessingServiceBackend processingServiceBackend = new ProcessingServiceBackend();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                processingServiceBackend.shutdown();
            }
        });
        processingServiceBackend.start();
    }

}
