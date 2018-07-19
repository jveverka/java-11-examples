package itx.futures.demo;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import itx.futures.demo.dto.DataInput;
import itx.futures.demo.dto.DataResult;

import java.util.concurrent.*;

public class DataProviderServiceImpl implements DataProviderService {

    private ListeningExecutorService executor;

    public DataProviderServiceImpl() {
        this.executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(4));
    }

    public DataProviderServiceImpl(ExecutorService executor) {
        this.executor = MoreExecutors.listeningDecorator(executor);
    }

    @Override
    public Future<DataResult> getData(DataInput dataInput) {
        CompletableFuture<DataResult> result = new CompletableFuture<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    computeTask(dataInput, result);
                } catch (ExecutionException e) {
                    result.completeExceptionally(e);
                }
            }
        });
        return result;
    }

    @Override
    public CompletionStage<DataResult> getDataStage(DataInput dataInput) {
        CompletableFuture<DataResult> result = new CompletableFuture<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    computeTask(dataInput, result);
                } catch (ExecutionException e) {
                    result.completeExceptionally(e);
                }
            }
        });
        return result;
    }

    @Override
    public ListenableFuture<DataResult> getDataListenable(DataInput dataInput) {
        return executor.submit(new Callable<DataResult>() {
            @Override
            public DataResult call() throws Exception {
                if (!dataInput.isExpectedToSucceed()) {
                    throw new ExecutionException(new DataProviderException("expected to fail"));
                }
                DataResult dataResult = new DataResult(dataInput.getInput(), Thread.currentThread().getName());
                return dataResult;
            }
        });
    }

    @Override
    public void close() throws Exception {
        this.executor.shutdown();
    }

    private void computeTask(DataInput dataInput, CompletableFuture<DataResult> result) throws ExecutionException {
        if (!dataInput.isExpectedToSucceed()) {
            throw new ExecutionException(new DataProviderException("expected to fail"));
        }
        DataResult dataResult = new DataResult(dataInput.getInput(), Thread.currentThread().getName());
        result.complete(dataResult);
    }

}
