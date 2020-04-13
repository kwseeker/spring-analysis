package top.kwseeker.spring.ioc.initialize;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanWithInitializationTest {

    @Test
    public void testBeanWithInitialization() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanWithInitialization.class);
        //关闭应用上下文，销毁bean
        applicationContext.close();
    }

}
