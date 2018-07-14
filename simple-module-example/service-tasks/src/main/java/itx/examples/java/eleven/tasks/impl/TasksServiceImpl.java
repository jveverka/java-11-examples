package itx.examples.java.eleven.tasks.impl;

import itx.examples.java.eleven.tasks.api.TasksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class TasksServiceImpl implements TasksService {

    final private static Logger LOG = LoggerFactory.getLogger(TasksServiceImpl.class);

    private final ExecutorService executor;

    public TasksServiceImpl() {
        LOG.info("initializing ...");
        executor = Executors.newFixedThreadPool(4);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        LOG.info("executing task ...");
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    T result = task.call();
                    completableFuture.complete(result);
                    LOG.info("task done OK");
                } catch (Exception e) {
                    completableFuture.completeExceptionally(e);
                    LOG.info("task ERROR");
                }
            }
        };
        executor.execute(runnable);
        LOG.info("task submitted");
        return completableFuture;
    }

    @Override
    public void shutdown() {
        LOG.info("shutdown");
        executor.shutdown();
    }

}
