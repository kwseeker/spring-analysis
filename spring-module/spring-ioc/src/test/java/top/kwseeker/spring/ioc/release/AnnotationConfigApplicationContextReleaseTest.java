package top.kwseeker.spring.ioc.release;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationConfigApplicationContextReleaseTest {

    @Test
    public void testConditionalAnnotation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        OuterBean outerBean = context.getBean("outerBean", OuterBean.class);
        context.close();
    }
}
