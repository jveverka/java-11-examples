package itx.examples.java.eleven.tasks.api;

import itx.examples.java.eleven.tasks.impl.TasksServiceImpl;

public class TasksServiceProvider {

    public static TasksService getTasksService() {
        return new TasksServiceImpl();
    }

}
