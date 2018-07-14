module itx.examples.java.eleven.tasks {
    exports itx.examples.java.eleven.tasks.api;
    provides itx.examples.java.eleven.tasks.api.TasksService with itx.examples.java.eleven.tasks.impl.TasksServiceImpl;
    requires org.slf4j;
}