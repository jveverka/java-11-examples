package itx.examples.java.eleven.tasks.api;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface TasksService {

    <T> Future<T> submit(Callable<T> task);

    void shutdown();

}
