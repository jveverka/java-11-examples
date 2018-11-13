package itx.examples.di.app.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import itx.examples.di.app.guice.module.AppModule;
import itx.examples.di.app.guice.service.MessageClientService;
import itx.examples.di.app.guice.service.MessageClientServiceImpl;
import itx.examples.di.guice.ExampleModule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static MessageClientServiceImpl messageClientService;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        ExampleModule exampleModule = new ExampleModule(executor);
        Injector injector = Guice.createInjector(exampleModule, new AppModule());

        messageClientService = injector.getInstance(MessageClientServiceImpl.class);
    }

    public static MessageClientService getMessageClientService() {
        return messageClientService;
    }

}
