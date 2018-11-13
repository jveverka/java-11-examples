package itx.examples.di.app.dagger.module;

import dagger.Module;
import dagger.Provides;
import itx.examples.di.app.dagger.service.MessageClientService;
import itx.examples.di.app.dagger.service.MessageClientServiceImpl;
import itx.examples.di.dagger.ExampleModule;

@Module(includes = ExampleModule.class)
public class AppModule {

    @Provides
    public MessageClientService getMessageService(MessageClientServiceImpl messageService) {
        return messageService;
    }

}
