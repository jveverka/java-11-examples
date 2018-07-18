package itx.examples.jetty.server.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import itx.examples.jetty.common.SystemUtils;
import itx.examples.jetty.common.dto.Message;
import itx.examples.jetty.common.services.MessageServiceAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataSyncServlet extends HttpServlet {

    private final static Logger LOG = LoggerFactory.getLogger(DataSyncServlet.class);

    private final ObjectMapper objectMapper;
    private final String baseURI;
    private final MessageServiceAsync messageService;

    public DataSyncServlet(String baseURI, MessageServiceAsync messageService) {
        LOG.info("init {}", baseURI);
        this.messageService = messageService;
        this.baseURI = baseURI;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        LOG.info("init ...");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("GET: {}", request.getRequestURI());
        try {
            String[] urlParameters = SystemUtils.getURLParameters(baseURI, request.getRequestURI());
            if (urlParameters.length > 0) {
                String groupId = urlParameters[0];
                Optional<List<Message>> messages = messageService.getMessages(groupId);
                if (messages.isPresent()) {
                    objectMapper.writeValue(response.getOutputStream(), messages.get());
                } else {
                    objectMapper.writeValue(response.getOutputStream(), new ArrayList<>());
                }
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("POST: {}", request.getRequestURI());
        try {
            Message message = objectMapper.readValue(request.getInputStream(), Message.class);
            messageService.publishMessage(message);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("PUT: {}", request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("DELETE: {}", request.getRequestURI());
        try {
            String[] urlParameters = SystemUtils.getURLParameters(baseURI, request.getRequestURI());
            if (urlParameters.length > 0) {
                String groupId = urlParameters[0];
                Optional<Long> messages = messageService.cleanMessages(groupId);
                if (messages.isPresent()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
                return;
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
