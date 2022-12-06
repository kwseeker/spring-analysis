package top.kwseeker.spring.ioc.release;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationConfigApplicationContextReleaseTest {

    @Test
    public void testConditionalAnnotation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.start();
        OuterBean outerBean = context.getBean("outerBean", OuterBean.class);
        //1内部通过close关闭
        context.close();
    }

    @Test
    public void testConditionalAnnotation2() throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        OuterBean outerBean = context.getBean("outerBean", OuterBean.class);
        //2外部通过信号关闭 (kill -2 <pid> kill -15 <pid>)
        Thread.sleep(100000);
    }
}
