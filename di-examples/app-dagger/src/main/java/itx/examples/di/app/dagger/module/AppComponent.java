package itx.examples.di.app.dagger.module;

import dagger.Component;
import itx.examples.di.app.dagger.service.MessageClientService;

import javax.inject.Singleton;

@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {

        MessageClientService messageClientService();

}
