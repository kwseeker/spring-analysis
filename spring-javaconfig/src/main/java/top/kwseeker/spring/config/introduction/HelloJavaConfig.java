package top.kwseeker.spring.config.introduction;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class HelloJavaConfig {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Service service = context.getBean(Service.class);
        service.doSomething();
        //Encryptor encryptor1 = context.getBean(Encryptor.class);
        //Encryptor encryptor2 = context.getBean(Encryptor.class);
        //assert !encryptor1.equals(encryptor2);

        context.registerShutdownHook();
    }
}
