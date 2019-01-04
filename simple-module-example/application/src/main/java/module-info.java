module itx.examples.java.eleven.application {
    requires itx.examples.java.eleven.compute;
    requires itx.examples.java.eleven.tasks;
    requires itx.examples.java.eleven.computeasync;
    requires org.slf4j;
    uses itx.examples.java.eleven.compute.api.ComputeService;
    uses itx.examples.java.eleven.computeasync.api.ComputeAsyncService;
    uses itx.examples.java.eleven.tasks.api.TasksService;
}