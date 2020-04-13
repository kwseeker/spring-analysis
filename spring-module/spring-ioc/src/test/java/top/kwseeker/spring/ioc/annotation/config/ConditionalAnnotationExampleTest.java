package top.kwseeker.spring.ioc.annotation.config;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConditionalAnnotationExampleTest {

    @Test
    public void testConditionalAnnotation() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ConditionalAnnotationExample.class);
        context.getBean("program");
    }
}
