package itx.examples.di.app.dagger;

import itx.examples.di.app.dagger.module.AppComponent;
import itx.examples.di.app.dagger.module.DaggerAppComponent;
import itx.examples.di.app.dagger.service.MessageClientService;
import itx.examples.di.dagger.ExampleModule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static MessageClientService messageClientService;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        AppComponent build = DaggerAppComponent.builder()
                .exampleModule(new ExampleModule(executor))
                .build();
        messageClientService = build.messageClientService();
    }

    public static MessageClientService getMessageClientService() {
        return messageClientService;
    }

}
