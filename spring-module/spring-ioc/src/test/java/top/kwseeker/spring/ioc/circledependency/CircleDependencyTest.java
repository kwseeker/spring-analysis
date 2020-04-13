package top.kwseeker.spring.ioc.circledependency;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CircleDependencyTest {

    @Test
    public void testConfigurationAnnotation() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(A.class, B.class);
        A a = (A)applicationContext.getBean("a");
        B b = (B)applicationContext.getBean("b");
    }
}
