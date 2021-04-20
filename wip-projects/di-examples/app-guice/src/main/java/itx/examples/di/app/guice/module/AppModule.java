package itx.examples.di.app.guice.module;

import com.google.inject.AbstractModule;
import itx.examples.di.app.guice.service.MessageClientService;
import itx.examples.di.app.guice.service.MessageClientServiceImpl;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MessageClientService.class).to(MessageClientServiceImpl.class);
    }

}
