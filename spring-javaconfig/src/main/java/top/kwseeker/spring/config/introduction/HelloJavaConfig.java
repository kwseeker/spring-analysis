package top.kwseeker.spring.config.introduction;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class HelloJavaConfig {

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Service service = context.getBean(Service.class);
        service.doSomething();
        //Encryptor encryptor1 = context.getBean(Encryptor.class);
        //Encryptor encryptor2 = context.getBean(Encryptor.class);
        //assert !encryptor1.equals(encryptor2);

        Service2 service2 = context.getBean(Service2.class);
        service2.doSomething();
        service2.doSomething();

        HttpService httpService = context.getBean(HttpService.class);
        httpService.requestBaidu();

        context.registerShutdownHook();
    }
}
