package itx.examples.di.app.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    private final ExecutorService executor;

    public AppConfig() {
        executor = Executors.newSingleThreadExecutor();
    }

    @Bean
    public ExecutorService getExecutorService() {
        return executor;
    }

}
