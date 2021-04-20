package itx.examples.di.app.spring.service;

import itx.examples.di.service.api.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MessageClientServiceImpl implements MessageClientService {

    private final MessageService messageSyncService;
    private final MessageService messageAsyncService;

    public MessageClientServiceImpl(@Autowired @Qualifier("sync") MessageService messageSyncService,
                                    @Autowired @Qualifier("async") MessageService messageAsyncService) {
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
