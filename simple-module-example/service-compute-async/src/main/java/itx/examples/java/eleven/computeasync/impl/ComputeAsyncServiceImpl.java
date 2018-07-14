package itx.examples.java.eleven.computeasync.impl;

import itx.examples.java.eleven.compute.api.ComputeService;
import itx.examples.java.eleven.computeasync.api.ComputeAsyncService;
import itx.examples.java.eleven.tasks.api.TasksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ComputeAsyncServiceImpl implements ComputeAsyncService {

    final private static Logger LOG = LoggerFactory.getLogger(ComputeAsyncServiceImpl.class);

    private final ComputeService computeService;
    private final TasksService tasksService;

    public ComputeAsyncServiceImpl() {
        LOG.info("initializing ...");
        ServiceLoader<ComputeService> loaderCs = ServiceLoader.load(ComputeService.class);
        Optional<ComputeService> computeServiceOptional = loaderCs.findFirst();
        if (computeServiceOptional.isPresent()) {
            computeService = computeServiceOptional.get();
        } else {
            computeService = null;
        }
        ServiceLoader<TasksService> loaderTs = ServiceLoader.load(TasksService.class);
        Optional<TasksService> taskServiceOptional = loaderTs.findFirst();
        if (taskServiceOptional.isPresent()) {
            tasksService = taskServiceOptional.get();
        } else {
            tasksService = null;
        }
    }

    @Override
    public Future<Float> addAsync(float... numbers) {
        LOG.info("addAsync ...");
        return tasksService.submit(new Callable<Float>() {
            @Override
            public Float call() throws Exception {
                return computeService.add(numbers);
            }
        });
    }

    @Override
    public void shutdown() {
        if (tasksService != null) {
            tasksService.shutdown();
        }
    }

}
