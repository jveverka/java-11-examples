package itx.examples.java.eleven.application;

import itx.examples.java.eleven.compute.api.ComputeService;
import itx.examples.java.eleven.compute.api.ComputeServiceProvider;
import itx.examples.java.eleven.tasks.api.TasksService;
import itx.examples.java.eleven.tasks.api.TasksServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.Callable;

public class Main {

    final private static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        LOG.info("MAIN start ...");
        Runtime.Version version = Runtime.version();
        LOG.info("Java version: {}", version.toString());
        ComputeService computeService = ComputeServiceProvider.getComputeService();
        TasksService tasksService = TasksServiceProvider.getTasksService();
        float computeResult = computeService.add(1,2,3,4);
        LOG.info("compute result {}", computeResult);
        String taskResult = tasksService.submit(getTask()).get();
        LOG.info("task result {}", taskResult);
        LOG.info("MAIN done.");
        tasksService.shutdown();
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

}
