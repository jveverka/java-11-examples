package itx.examples.springboot.di.config;

import itx.examples.springboot.di.services.DataService;
import itx.examples.springboot.di.services.PrintService;
import itx.examples.springboot.di.services.PrintServiceStdErr;
import itx.examples.springboot.di.services.SuperService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppConfig {

    private final SuperService superService;
    private final PrintService printServiceStdErr;

    public AppConfig() {
        this.superService = new SuperService();
        this.printServiceStdErr = new PrintServiceStdErr();
    }

    @Bean(name="stdOut")
    @Primary
    public PrintService createOutPrintService() {
        return superService;
    }

    @Bean(name="stdErr")
    public PrintService createErrPrintService() {
        return printServiceStdErr;
    }

    @Bean
    public DataService createSuperService() {
        return superService;
    }


}
