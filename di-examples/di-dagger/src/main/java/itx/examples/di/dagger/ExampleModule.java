package itx.examples.di.dagger;

import dagger.Module;
import dagger.Provides;
import itx.examples.di.service.api.MessageService;
import itx.examples.di.service.impl.MessageServiceAsyncImpl;
import itx.examples.di.service.impl.MessageServiceSyncImpl;

import javax.inject.Named;
import java.util.concurrent.ExecutorService;

@Module
public class ExampleModule {

    private final MessageService messageSyncService;
    private final MessageService messageAsyncService;

    public ExampleModule(ExecutorService executor) {
        this.messageSyncService = new MessageServiceSyncImpl();
        this.messageAsyncService = new MessageServiceAsyncImpl(executor);
    }

    @Provides
    @Named("sync")
    public MessageService getMessageSyncService() {
        return messageSyncService;
    }

    @Provides
    @Named("async")
    public MessageService getMessageAsyncService() {
        return messageAsyncService;
    }

}
