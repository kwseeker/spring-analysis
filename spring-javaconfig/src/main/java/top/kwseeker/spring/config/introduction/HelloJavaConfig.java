package top.kwseeker.spring.config.introduction;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class HelloJavaConfig {

    public static void main(String[] args) throws IOException {
        //指定多个@Configuration配置类
        //AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class, HttpConfig.class);
        //通过指定package自动扫描获取配置类 (classpath*:top/kwseeker/spring/config/introduction/**/*.class)
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("top.kwseeker.spring.config.introduction");
        //通配符匹配package,然后自动扫描获取配置类 (好像不太好用，解析出来的package位置是错的：classpath*:**/config/introduction/*/class/**/*.class)
        //AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("**/config/introduction/*.class");

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
