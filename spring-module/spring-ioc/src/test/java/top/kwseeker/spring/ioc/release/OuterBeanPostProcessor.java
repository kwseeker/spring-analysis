package top.kwseeker.spring.ioc.release;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class OuterBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("outerBean")) {
            System.out.println("call postProcessBeforeInitialization()...");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("outerBean")) {
            System.out.println("call postProcessAfterInitialization()...");
        }
        return bean;
    }
}
