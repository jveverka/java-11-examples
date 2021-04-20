package itx.examples.di.app.dagger.service;

import itx.examples.di.service.api.MessageService;

public interface MessageClientService {

    MessageService getMessageSyncService();

    MessageService getMessageAsyncService();

}
