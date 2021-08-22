package top.kwseeker.spring.aopapi;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.kwseeker.spring.common.Calculate;

/**
 * Spring1.2 AOP
 */
public class FirstGenerationAopMain {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Calculate calculate = context.getBean("calculateProxy", Calculate.class);
        System.out.println(calculate.getClass());
        int sum = calculate.add(1, 1);
        System.out.println(sum);
    }
}
