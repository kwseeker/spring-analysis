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

    //测试时发现同样是global开头的这个前置通知没有被加入到Advisor链。
    //原因：参考 ProxyFactoryBean$addGlobalAdvisor(ListableBeanFactory beanFactory, String prefix)方法，
    //发现：只有 Advisor 和 Interceptor 类型的Bean会进行全局匹配
    @Bean
    public CountBeforeAdvice globalCountBeforeAdvice() {
        return new CountBeforeAdvice();
    }

    @Bean
    public LogInterceptor globalLogInterceptor() {
        return new LogInterceptor();
    }

    @Bean
    public ProxyFactoryBean calculateProxy(Calculate calculate) {
        ProxyFactoryBean proxyFactory = new ProxyFactoryBean();
        //第三个是全局
        proxyFactory.setInterceptorNames("logBeforeAdvice", "logInterceptor", "global*");
        proxyFactory.setTarget(calculate);
        return proxyFactory;
    }
}
