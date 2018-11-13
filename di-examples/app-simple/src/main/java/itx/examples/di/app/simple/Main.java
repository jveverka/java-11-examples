package itx.examples.di.app.simple;

import itx.examples.di.service.api.MessageRequest;
import itx.examples.di.service.api.MessageService;
import itx.examples.di.service.impl.MessageServiceAsyncImpl;
import itx.examples.di.service.impl.MessageServiceSyncImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LOG.info("Main");
        MessageRequest messageRequest = new MessageRequest("Tom");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        MessageService messageServiceSync = new MessageServiceSyncImpl();
        MessageService messageServiceAsync = new MessageServiceAsyncImpl(executor);
        LOG.info("From sync : {}", messageServiceSync.getMessage(messageRequest).get().getResponse());
        LOG.info("From async: {}", messageServiceAsync.getMessage(messageRequest).get().getResponse());

        try {
            messageServiceSync.failWithException();
        } catch (Exception e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }

}
