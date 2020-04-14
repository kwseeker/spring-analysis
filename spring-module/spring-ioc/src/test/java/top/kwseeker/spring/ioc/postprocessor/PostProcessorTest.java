package top.kwseeker.spring.ioc.postprocessor;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.kwseeker.spring.ioc.entity.User;

public class PostProcessorTest {

    @Test
    public void testPostProcessor() {
        ApplicationContext context = new AnnotationConfigApplicationContext(MyBeanFactoryPostProcessor.class, MyBeanPostProcessor.class);
        User user = (User) context.getBean("user");
    }

}
