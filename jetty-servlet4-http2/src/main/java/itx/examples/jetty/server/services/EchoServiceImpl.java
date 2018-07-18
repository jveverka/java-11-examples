package itx.examples.jetty.server.services;

import itx.examples.jetty.common.services.EchoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServiceImpl implements EchoService {

    final private static Logger LOG = LoggerFactory.getLogger(EchoServiceImpl.class);

    public EchoServiceImpl() {
        LOG.info("service init ...");
    }

    @Override
    public String ping(String message) {
        LOG.info("echo:{}", message);
        return "echo:" + message;
    }

}
