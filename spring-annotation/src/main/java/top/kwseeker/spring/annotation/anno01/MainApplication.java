package top.kwseeker.spring.annotation.anno01;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApplication {

    public static void main(String[] args) {
        //ApplicationContext context = new AnnotationConfigApplicationContext("./");  //类扫描成功, classpath*:///**/*.class
        //ApplicationContext context = new AnnotationConfigApplicationContext("//");  //类扫描成功, classpath*:///**/*.class
        //ApplicationContext context = new AnnotationConfigApplicationContext(".");   //类扫描失败, "///"（classpath*:////**/*.class） "/"(classpath*://**/*.class) 都是失败
        ApplicationContext context = new AnnotationConfigApplicationContext("top.kwseeker.spring.annotation.anno01");  //类扫描成功, classpath*:top/kwseeker/spring/annotation/anno01/**/*.class
        BizService bizService = context.getBean(BizService.class);
        bizService.doBiz();
    }
}
