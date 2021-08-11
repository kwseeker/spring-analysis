package top.kwseeker.spring.config.introduction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Service service() {
        //构造器注入
        return new ServiceImpl(repository());
    }

    @Bean
    public Repository repository() {
        return new JdbcRepository();
    }
}
