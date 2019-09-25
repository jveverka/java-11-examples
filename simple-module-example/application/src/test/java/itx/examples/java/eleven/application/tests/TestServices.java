package itx.examples.java.eleven.application.tests;

import itx.examples.java.eleven.application.utils.Utils;
import itx.examples.java.eleven.compute.api.ComputeService;
import itx.examples.java.eleven.computeasync.api.ComputeAsyncService;
import itx.examples.java.eleven.tasks.api.TasksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestServices {

    final private static Logger LOG = LoggerFactory.getLogger(TestServices.class);

    private ComputeAsyncService computeAsyncService;
    private ComputeService computeService;
    private TasksService tasksService;

    @BeforeClass
    public void setup() {
        LOG.info("test setup ...");
        computeAsyncService = Utils.getServiceOrFail(ComputeAsyncService.class);
        computeService = Utils.getServiceOrFail(ComputeService.class);
        tasksService = Utils.getServiceOrFail(TasksService.class);
        Assert.assertNotNull(computeAsyncService);
        Assert.assertNotNull(computeService);
        Assert.assertNotNull(tasksService);
    }

    @Test
    public void testComputeService() {
        LOG.info("testing compute service");
        float result = computeService.add(1, 2, 3, 4);
        Assert.assertEquals(result, 10F);
    }

    @Test
    public void testComputeAsyncService() throws ExecutionException, InterruptedException {
        LOG.info("testing compute async service");
        Future<Float> upcommingResult = computeAsyncService.addAsync(1, 2, 3, 4);
        Assert.assertNotNull(upcommingResult);
        Float result = upcommingResult.get();
        Assert.assertNotNull(result);
        Assert.assertEquals(result, Float.valueOf(10F));
    }

    @Test
    public void testTasksService() throws ExecutionException, InterruptedException {
        LOG.info("testing task service");
        Callable<String> simpleTask = Utils.getSimpleTask();
        Future<String> submit = tasksService.submit(simpleTask);
        String result = submit.get();
        Assert.assertNotNull(result);
        Assert.assertTrue(!result.isBlank());
        Assert.assertTrue(!result.isEmpty());
    }

    @AfterClass
    public void shutdown() {
        LOG.info("test shutdown.");
        computeAsyncService.shutdown();
        tasksService.shutdown();
    }

}
