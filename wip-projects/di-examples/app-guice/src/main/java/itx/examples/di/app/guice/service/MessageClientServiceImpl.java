package itx.examples.di.app.guice.service;

import itx.examples.di.service.api.MessageService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class MessageClientServiceImpl implements MessageClientService {

    @Inject
    @Named("sync")
    private MessageService messageSyncService;

    @Inject
    @Named("async")
    private MessageService messageAsyncService;

    @Override
    public MessageService getMessageSyncService() {
        return messageSyncService;
    }

    @Override
    public MessageService getMessageAsyncService() {
        return messageAsyncService;
    }

}
