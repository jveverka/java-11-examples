package itx.examples.java.eleven.application;

import itx.examples.java.eleven.application.utils.Utils;
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
        ComputeAsyncService computeAsyncService = Utils.getServiceOrFail(ComputeAsyncService.class);
        ComputeService computeService = Utils.getServiceOrFail(ComputeService.class);
        TasksService tasksService = Utils.getServiceOrFail(TasksService.class);
        float computeResult = computeService.add(1,2,3,4);
        LOG.info("compute result {}", computeResult);
        String taskResult = tasksService.submit(Utils.getSimpleTask()).get();
        LOG.info("task result {}", taskResult);
        Future<Float> floatFuture = computeAsyncService.addAsync(1, 2, 3, 4);
        LOG.info("compute async result {}", floatFuture.get());
        LOG.info("MAIN done.");
        tasksService.shutdown();
        computeAsyncService.shutdown();
    }

}
