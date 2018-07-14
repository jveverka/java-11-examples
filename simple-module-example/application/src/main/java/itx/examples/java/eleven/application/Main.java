package itx.examples.java.eleven.application;

import itx.examples.java.eleven.compute.api.ComputeService;
import itx.examples.java.eleven.computeasync.api.ComputeAsyncService;
import itx.examples.java.eleven.tasks.api.TasksService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class Main {

    final private static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        LOG.info("MAIN start ...");
        Runtime.Version version = Runtime.version();
        LOG.info("Java version: {}", version.toString());
        ComputeAsyncService computeAsyncService = getServiceOrFail(ComputeAsyncService.class);
        ComputeService computeService = getServiceOrFail(ComputeService.class);
        TasksService tasksService = getServiceOrFail(TasksService.class);
        float computeResult = computeService.add(1,2,3,4);
        LOG.info("compute result {}", computeResult);
        String taskResult = tasksService.submit(getTask()).get();
        LOG.info("task result {}", taskResult);
        Future<Float> floatFuture = computeAsyncService.addAsync(1, 2, 3, 4);
        LOG.info("compute async result {}", floatFuture.get());
        LOG.info("MAIN done.");
        tasksService.shutdown();
        computeAsyncService.shutdown();
    }

    private static Callable<String> getTask() {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                String result = "";
                LOG.info("task start");
                result = (new Date()).toString();
                LOG.info("task done");
                return result;
            }
        };
    }

    private static <T> T getServiceOrFail(Class<T> type) {
        ServiceLoader<T> loader = ServiceLoader.load(type);
        Optional<T> serviceOptional = loader.findFirst();
        if (serviceOptional.isPresent()) {
            return serviceOptional.get();
        } else {
            throw new UnsupportedOperationException("service not found");
        }
    }

}
