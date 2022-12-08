package top.kwseeker.spring.ioc.close;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class AppConfig {

    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    public OuterBean outerBean() {
        return new OuterBean();
    }

    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    public DBean DBean() {
        return new DBean();
    }

    @Bean
    public EBean EBean() {
        return new EBean();
    }
}
