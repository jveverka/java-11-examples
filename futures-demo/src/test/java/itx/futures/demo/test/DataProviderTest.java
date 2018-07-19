package itx.futures.demo.test;

import com.google.common.util.concurrent.ListenableFuture;
import itx.futures.demo.DataProviderService;
import itx.futures.demo.DataProviderServiceImpl;
import itx.futures.demo.dto.DataInput;
import itx.futures.demo.dto.DataResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.*;
import java.util.function.BiConsumer;

public class DataProviderTest {

    private DataProviderService dataProvider;
    private ExecutorService localExecutor;

    @BeforeClass
    public void setup() {
        this.dataProvider = new DataProviderServiceImpl();
        this.localExecutor = Executors.newSingleThreadExecutor();
    }

    @DataProvider(name = "testDataProvider")
    public Object[][] createData1() {
        return new Object[][] {
                { Boolean.TRUE },
                { Boolean.FALSE },
        };
    }


    @Test(dataProvider = "testDataProvider")
    public void testDataProviderFuture(Boolean expectedToSucceed) throws InterruptedException, TimeoutException {
        String currentThreadName = Thread.currentThread().getName();
        DataInput dataInput = new DataInput(currentThreadName, expectedToSucceed);
        Future<DataResult> futureData = dataProvider.getData(dataInput);
        try {
            DataResult dataResult = futureData.get(15, TimeUnit.SECONDS);
            Assert.assertTrue(futureData.isDone());
            Assert.assertNotNull(dataResult);
            Assert.assertNotNull(dataResult.getInput());
            Assert.assertNotNull(dataResult.getResult());
            Assert.assertNotEquals(dataResult.getInput(), dataResult.getResult());
        } catch (ExecutionException e) {
            Assert.assertFalse(expectedToSucceed);
        }
    }

    @Test(dataProvider = "testDataProvider")
    public void testDataStage(Boolean expectedToSucceed) throws InterruptedException {
        String currentThreadName = Thread.currentThread().getName();
        DataInput dataInput = new DataInput(currentThreadName, expectedToSucceed);
        CompletionStage<DataResult> dataStage = dataProvider.getDataStage(dataInput);
        Assert.assertNotNull(dataStage);
        TestBiConsumer testBiConsumer = new TestBiConsumer();
        dataStage.whenComplete(testBiConsumer);
        try {
            DataResult dataResult = testBiConsumer.getAndWait(15, TimeUnit.SECONDS);
            Assert.assertNotNull(dataResult);
            Assert.assertNotNull(dataResult.getInput());
            Assert.assertNotNull(dataResult.getResult());
            Assert.assertNotEquals(dataResult.getInput(), dataResult.getResult());
        } catch (ExecutionException e) {
            Assert.assertFalse(expectedToSucceed);
        }
    }

    @Test(dataProvider = "testDataProvider")
    public void testDataListenable(Boolean expectedToSucceed) throws InterruptedException {
        String currentThreadName = Thread.currentThread().getName();
        DataInput dataInput = new DataInput(currentThreadName, expectedToSucceed);
        ListenableFuture<DataResult> dataListenableFuture = dataProvider.getDataListenable(dataInput);
        TestRunnableListener testRunnableListener = new TestRunnableListener(dataListenableFuture);
        dataListenableFuture.addListener(testRunnableListener, localExecutor);
        Assert.assertNotNull(dataListenableFuture);
        try {
            DataResult dataResult = testRunnableListener.getAndWait(15, TimeUnit.SECONDS);
            Assert.assertNotNull(dataResult);
            Assert.assertNotNull(dataResult.getInput());
            Assert.assertNotNull(dataResult.getResult());
            Assert.assertNotEquals(dataResult.getInput(), dataResult.getResult());
        } catch (ExecutionException e) {
            Assert.assertFalse(expectedToSucceed);
        }
    }

    @AfterClass
    public void shutdown() throws Exception {
        this.dataProvider.close();
        this.localExecutor.shutdown();
    }

    private static class TestBiConsumer implements BiConsumer<DataResult, Throwable> {
        private CountDownLatch cl;
        private DataResult dataResult;
        private Throwable throwable;

        public TestBiConsumer() {
            this.cl = new CountDownLatch(1);
        }

        @Override
        public void accept(DataResult dataResult, Throwable throwable) {
            this.dataResult = dataResult;
            this.throwable = throwable;
            this.cl.countDown();
        }

        public DataResult getAndWait(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException {
            cl.await(timeout, unit);
            if (throwable != null) {
                throw new ExecutionException(throwable);
            }
            return dataResult;
        }
    }

    private static class TestRunnableListener implements Runnable {
        private CountDownLatch cl;
        private ListenableFuture<DataResult> dataListenableFuture;

        public TestRunnableListener(ListenableFuture<DataResult> dataListenableFuture) {
            this.cl = new CountDownLatch(1);
            this.dataListenableFuture = dataListenableFuture;
        }

        @Override
        public void run() {
            this.cl.countDown();
        }

        public DataResult getAndWait(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException {
            cl.await(timeout, unit);
            return dataListenableFuture.get();
        }
    }

}
