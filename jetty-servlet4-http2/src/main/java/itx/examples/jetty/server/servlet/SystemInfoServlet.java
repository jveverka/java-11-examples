package itx.examples.jetty.server.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import itx.examples.jetty.common.services.SystemInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SystemInfoServlet extends HttpServlet {

    private final static Logger LOG = LoggerFactory.getLogger(SystemInfoServlet.class);

    private final String baseURI;
    private final ObjectMapper objectMapper;
    private final SystemInfoService systemInfoService;

    public SystemInfoServlet(String baseURI, SystemInfoService systemInfoService) {
        LOG.info("init {}", baseURI);
        this.baseURI = baseURI;
        this.objectMapper = new ObjectMapper();
        this.systemInfoService = systemInfoService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        LOG.info("init ...");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("servlet: get getSystemInfo");
        objectMapper.writeValue(response.getOutputStream(), systemInfoService.getSystemInfo());
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
