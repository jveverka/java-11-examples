package itx.examples.jetty.server.servlet;

import itx.examples.jetty.common.services.MessageServiceAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class DataAsyncServlet extends HttpServlet {

    final private static Logger LOG = LoggerFactory.getLogger(DataAsyncServlet.class);

    private final String baseURI;
    private final Queue<AsyncContext> ongoingRequests;
    private final ExecutorService service;
    private final MessageServiceAsync messageService;

    public DataAsyncServlet(String baseURI, MessageServiceAsync messageService) {
        LOG.info("init {}", baseURI);
        this.baseURI = baseURI;
        this.ongoingRequests = new ConcurrentLinkedQueue<>();
        this.service = Executors.newFixedThreadPool(10);
        this.messageService = messageService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        LOG.info("init ...");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        final AsyncContext ac = request.startAsync();
        ac.setTimeout(60 * 1000);
        ac.addListener(new AsyncListener() {
            @Override public void onComplete(AsyncEvent event) throws IOException {ongoingRequests.remove(ac);}
            @Override public void onTimeout(AsyncEvent event) throws IOException {ongoingRequests.remove(ac);}
            @Override public void onError(AsyncEvent event) throws IOException {ongoingRequests.remove(ac);}
            @Override public void onStartAsync(AsyncEvent event) throws IOException {}
        });
        ongoingRequests.add(ac);
    }

}
