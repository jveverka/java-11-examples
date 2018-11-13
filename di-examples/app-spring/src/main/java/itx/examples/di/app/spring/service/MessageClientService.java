package itx.examples.di.app.spring.service;

import itx.examples.di.service.api.MessageService;

public interface MessageClientService {

    public MessageService getMessageSyncService();

    public MessageService getMessageAsyncService();

}
