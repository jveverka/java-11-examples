package itx.examples.java.eleven.compute.impl;

import itx.examples.java.eleven.compute.api.ComputeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComputeServiceImpl implements ComputeService {

    final private static Logger LOG = LoggerFactory.getLogger(ComputeServiceImpl.class);

    public ComputeServiceImpl() {
        LOG.info("initializing ...");
    }

    @Override
    public float add(float... numbers) {
        LOG.info("computing ...");
        float result = 0;
        for (float number: numbers) {
            result = result + number;
        }
        return result;
    }

}
