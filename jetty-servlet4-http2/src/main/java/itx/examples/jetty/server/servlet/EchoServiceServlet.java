package itx.examples.jetty.server.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import itx.examples.jetty.common.SystemUtils;
import itx.examples.jetty.common.dto.EchoMessage;
import itx.examples.jetty.common.services.EchoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EchoServiceServlet extends HttpServlet {

    private final static Logger LOG = LoggerFactory.getLogger(EchoServiceServlet.class);

    private final String baseURI;
    private final ObjectMapper objectMapper;
    private final EchoService echoService;

    public EchoServiceServlet(String baseURI, EchoService echoService) {
        LOG.info("init {}", baseURI);
        this.objectMapper = new ObjectMapper();
        this.echoService = echoService;
        this.baseURI = baseURI;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        LOG.info("init ...");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("servlet: get ping");
        String[] urlParameters = SystemUtils.getURLParameters(baseURI, request.getRequestURI());
        String responseMsg = echoService.ping(urlParameters[0]);
        EchoMessage echoResponse = new EchoMessage(responseMsg);
        objectMapper.writeValue(response.getOutputStream(), echoResponse);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("servlet: post ping");
        EchoMessage echoRequest = objectMapper.readValue(request.getInputStream(), EchoMessage.class);
        String responseMsg = echoService.ping(echoRequest.getMessage());
        EchoMessage echoResponse = new EchoMessage(responseMsg);
        objectMapper.writeValue(response.getOutputStream(), echoResponse);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
