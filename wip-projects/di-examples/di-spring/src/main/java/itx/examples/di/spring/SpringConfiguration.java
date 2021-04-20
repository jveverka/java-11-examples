package itx.examples.di.spring;

import itx.examples.di.service.api.MessageService;
import itx.examples.di.service.impl.MessageServiceAsyncImpl;
import itx.examples.di.service.impl.MessageServiceSyncImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

@Configuration
public class SpringConfiguration {

    private final MessageService messageSyncService;
    private final MessageService messageAsyncService;

    public SpringConfiguration(@Autowired ExecutorService executor) {
        this.messageSyncService = new MessageServiceSyncImpl();
        this.messageAsyncService = new MessageServiceAsyncImpl(executor);
    }

    @Bean
    public MessageService getMessageService() {
        return messageSyncService;
    }

    @Bean("sync")
    public MessageService getSyncMessageService() {
        return messageSyncService;
    }

    @Bean("async")
    public MessageService getAsyncMessageService() {
        return messageAsyncService;
    }

}
