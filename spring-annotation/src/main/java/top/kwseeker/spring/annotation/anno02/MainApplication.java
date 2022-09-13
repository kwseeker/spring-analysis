package top.kwseeker.spring.annotation.anno02;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class MainApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("top.kwseeker.spring.annotation.anno02");  //类扫描成功, classpath*:top/kwseeker/spring/annotation/anno02/**/*.class
        Map<String, SomeBean> someBeanMap = context.getBeansOfType(SomeBean.class);
        BeanDefinition myConfigBd = context.getBeanDefinition("myConfig");
        System.out.println(myConfigBd.getBeanClassName());
    }
}
