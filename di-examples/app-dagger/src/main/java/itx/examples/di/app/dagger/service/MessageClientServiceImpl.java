package itx.examples.di.app.dagger.service;

import itx.examples.di.service.api.MessageService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class MessageClientServiceImpl implements MessageClientService {

    private final MessageService messageSyncService;
    private final MessageService messageAsyncService;

    @Inject
    public MessageClientServiceImpl(@Named("sync") MessageService messageSyncService,
                                    @Named("async") MessageService messageAsyncService) {
        this.messageSyncService = messageSyncService;
        this.messageAsyncService = messageAsyncService;
    }


    @Override
    public MessageService getMessageSyncService() {
        return messageSyncService;
    }

    @Override
    public MessageService getMessageAsyncService() {
        return messageAsyncService;
    }

}
