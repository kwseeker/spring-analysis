package top.kwseeker.spring.ioc.lazyinit;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LazyPrototypeBeanTest {

    @Test
    public void testLazyPrototypeBean() throws InterruptedException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(LazyPrototypeBean.class);
        Thread.sleep(5000);
        applicationContext.getBean("lazyPrototypeBean");
    }
}
