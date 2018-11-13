package itx.examples.di.app.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("itx.examples.di.spring")
@ComponentScan("itx.examples.di.app")
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Main.class, args);
        run.registerShutdownHook();
    }

}
