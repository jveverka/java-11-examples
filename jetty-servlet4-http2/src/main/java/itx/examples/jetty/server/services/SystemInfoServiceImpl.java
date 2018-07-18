package itx.examples.jetty.server.services;

import itx.examples.jetty.common.dto.SystemInfo;
import itx.examples.jetty.common.services.SystemInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemInfoServiceImpl implements SystemInfoService {

    final private static Logger LOG = LoggerFactory.getLogger(SystemInfoServiceImpl.class);

    public SystemInfoServiceImpl() {
        LOG.info("service init ...");
    }

    @Override
    public SystemInfo getSystemInfo() {
        LOG.info("getSystemInfo");
        return new SystemInfo(System.currentTimeMillis(), "jetty-http2-demo", "1.0.0");
    }

}
