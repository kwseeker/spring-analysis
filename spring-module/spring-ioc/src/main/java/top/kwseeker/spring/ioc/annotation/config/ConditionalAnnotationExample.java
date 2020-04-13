package top.kwseeker.spring.ioc.annotation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import top.kwseeker.spring.ioc.entity.Program;
import top.kwseeker.spring.ioc.entity.User;

@Configuration
public class ConditionalAnnotationExample {

    //TODO:　如何按＠Conditional顺序加载Bean?
    @Bean
    public User user() {
        System.out.println("bean user created!");
        return new User();
    }

    @Bean
    @Conditional(UserConditional.class)
    public Program program() {
        System.out.println("bean program created!");
        return new Program();
    }
}
