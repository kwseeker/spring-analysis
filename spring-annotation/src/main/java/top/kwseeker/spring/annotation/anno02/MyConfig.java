package top.kwseeker.spring.annotation.anno02;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//@Component
@Configuration
public class MyConfig {

    @Bean
    public CommonBean commonBean() {
        return new CommonBean();
    }

    @Bean
    public SomeBean someBean1() {
        SomeBean bean = new SomeBean();
        bean.setCommonBean(commonBean());
        System.out.println("someBean1: " + System.identityHashCode(bean.getCommonBean()));
        return bean;
    }

    @Bean
    public SomeBean someBean2() {
        SomeBean bean = new SomeBean();
        bean.setCommonBean(commonBean());
        System.out.println("someBean2: " + System.identityHashCode(bean.getCommonBean()));
        return bean;
    }
}
