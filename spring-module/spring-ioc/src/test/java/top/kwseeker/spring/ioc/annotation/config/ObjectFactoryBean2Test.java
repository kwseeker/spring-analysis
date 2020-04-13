package top.kwseeker.spring.ioc.annotation.config;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ObjectFactoryBean2Test {

    @Test
    public void testAnnotatedFactoryBean() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        Object obj1 = applicationContext.getBean("objectFactoryBean2");
        Object obj2 = applicationContext.getBean("&objectFactoryBean2");
        System.out.println(obj1 instanceof Integer);
        System.out.println(obj2 instanceof ObjectFactoryBean2);
    }
}
