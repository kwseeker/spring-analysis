package top.kwseeker.spring.ioc.close;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class OuterBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("outerBean")) {
            System.out.println("call OuterBeanPostProcessor postProcessBeforeInitialization()...");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("outerBean")) {
            System.out.println("call OuterBeanPostProcessor postProcessAfterInitialization()...");
        }
        return bean;
    }
}
