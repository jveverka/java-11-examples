package itx.examples.di.app.guice.service;

import itx.examples.di.service.api.MessageService;

public interface MessageClientService {

    MessageService getMessageSyncService();

    MessageService getMessageAsyncService();

}
