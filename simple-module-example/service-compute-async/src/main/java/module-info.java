module itx.examples.java.eleven.computeasync {
    exports itx.examples.java.eleven.computeasync.api;
    provides itx.examples.java.eleven.computeasync.api.ComputeAsyncService with itx.examples.java.eleven.computeasync.impl.ComputeAsyncServiceImpl;
    requires itx.examples.java.eleven.compute;
    requires itx.examples.java.eleven.tasks;
    requires org.slf4j;
    uses itx.examples.java.eleven.compute.api.ComputeService;
    uses itx.examples.java.eleven.tasks.api.TasksService;
}