package top.kwseeker.spring.config.introduction;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class HelloJavaConfig {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Service service = context.getBean(Service.class);
        service.doSomething();

        context.registerShutdownHook();
    }
}
