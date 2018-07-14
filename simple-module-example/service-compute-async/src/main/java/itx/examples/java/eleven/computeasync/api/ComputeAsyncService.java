package itx.examples.java.eleven.computeasync.api;

import java.util.concurrent.Future;

public interface ComputeAsyncService {

    Future<Float> addAsync(float ... numbers);

    void shutdown();

}
