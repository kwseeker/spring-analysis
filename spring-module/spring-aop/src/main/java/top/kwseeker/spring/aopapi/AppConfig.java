package top.kwseeker.spring.aopapi;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import top.kwseeker.spring.common.Calculate;
import top.kwseeker.spring.common.SimpleCalculator;

public class AppConfig {

    @Bean
    public Calculate simpleCalculator() {
        return new SimpleCalculator();
    }

    @Bean
    public LogBeforeAdvice logBeforeAdvice(){
        return new LogBeforeAdvice();
    }

    //Interceptor方式，可以理解为环绕通知
    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Bean
    public ProxyFactoryBean calculateProxy(Calculate calculate) {
        ProxyFactoryBean proxyFactory = new ProxyFactoryBean();
        proxyFactory.setInterceptorNames("logBeforeAdvice", "logInterceptor");
        proxyFactory.setTarget(calculate);
        return proxyFactory;
    }
}
