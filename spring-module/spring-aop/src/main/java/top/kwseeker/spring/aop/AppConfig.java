package top.kwseeker.spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@Configuration
@ComponentScan
@EnableAspectJAutoProxy     //注解方式启用AspectJ风格的切面，也仅仅是用了AspectJ的注解、切入点解析和匹配。
public class AppConfig {

    @Bean
    public Object useless() {
        //这个方法也会被切面拦截
        log.info("create a useless object ...");
        return new Object();
    }
}
