package itx.examples.di.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import itx.examples.di.service.api.MessageService;
import itx.examples.di.service.impl.MessageServiceAsyncImpl;
import itx.examples.di.service.impl.MessageServiceSyncImpl;

import java.util.concurrent.ExecutorService;

public class ExampleModule extends AbstractModule {

    private final MessageService messageSyncService;
    private final MessageService messageAsyncService;

    public ExampleModule(ExecutorService executor) {
        this.messageSyncService = new MessageServiceSyncImpl();
        this.messageAsyncService = new MessageServiceAsyncImpl(executor);
    }

    @Override
    protected void configure() {
        bind(MessageService.class).annotatedWith(Names.named("sync")).toInstance(messageSyncService);
        bind(MessageService.class).annotatedWith(Names.named("async")).toInstance(messageAsyncService);
    }

}
